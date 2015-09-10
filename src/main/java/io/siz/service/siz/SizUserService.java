package io.siz.service.siz;

import io.siz.domain.siz.SizToken;
import io.siz.domain.siz.SizUser;
import io.siz.exception.SizException;
import io.siz.repository.siz.SizTokenRepository;
import io.siz.repository.siz.SizUserRepository;
import io.siz.web.rest.dto.siz.SizUserDTO;
import java.util.Date;
import java.util.Optional;
import java.util.function.Supplier;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;
import static org.springframework.util.StringUtils.*;

/**
 *
 * @author fred
 */
@Service
public class SizUserService {

    private final Logger log = LoggerFactory.getLogger(SizUserService.class);

    @Inject
    private SizUserRepository sizUserRepository;
    @Inject
    private SizTokenRepository sizTokenRepository;
    @Inject
    private PasswordEncoder passwordEncoder;

    @PreAuthorize("hasRole('ROLE_USER')")
    public Optional<SizUser> create(SizUserDTO userDTO, String tokenString) {
        // 1 on récupère le token complet du type.
        SizToken token = sizTokenRepository.findById(tokenString).get();
        // 2 on regarde si ce token est déja accroché à un user
        if (!isEmpty(token.getUserId())) {
            log.error("A user already exist with this token");
            throw new SizException();
        } else {
            SizUser user = new SizUser();

            user.setPasswordHash(passwordEncoder.encode(userDTO.getPassword()));
            user.setUsername(userDTO.getUsername());
            user.setFacebookToken(userDTO.getFacebookToken());
            user.setCreationDate(new Date());
            // create by email
            user.setEmail(userDTO.getEmail());
            // create by facebook
            if (hasLength(userDTO.getFacebookToken())) {
                FacebookTemplate template = new FacebookTemplate(userDTO.getFacebookToken());
                /**
                 * on teste la verif de l'email.
                 */
                user.setEmail(template.userOperations().getUserProfile().getEmail());
                /**
                 * mais on vuet surtout garder son facebookUserId.
                 */
                user.setFacebookUserId(user.getFacebookUserId());
            }

            try {
                user = sizUserRepository.insert(user);
                token.setUserId(user.getId());

            } catch (Exception e) {
                log.error("exception caught : {}", e.getMessage());
                throw e;
            }

        }
        return Optional.empty();
    }

    /**
     * Le Principal est positionné là dans le
     * {@link io.siz.security.siz.SizAuthTokenFilter} et il s'agit d'un
     * {@link org.springframework.security.core.userdetails.User}
     *
     * principal.username est le tokenString
     *
     * @param userId
     * @return le user demandé si les droits sont suffisants.
     */
    @PreAuthorize("principal.userId == #userId or hasRole('ROLE_ADMIN')")
    public SizUser getUser(String userId) {
        return sizUserRepository.findOne(userId);
    }

    public Optional<SizUser> loginUser(SizUserDTO dto) {

        Supplier<Optional<SizUser>> r;
        if (!isEmpty(dto.getEmail()) && !isEmpty(dto.getPassword())) {
            r = () -> sizUserRepository.findByEmail(dto.getEmail());
        } else if (!isEmpty(dto.getUsername()) && !isEmpty(dto.getPassword())) {
            r = () -> sizUserRepository.findByUsername(dto.getUsername());
        } else if (!isEmpty(dto.getFacebookToken())) {
            r = () -> sizUserRepository.findFacebookUserId(dto.getFacebookToken());
        } else {
            throw new SizException();
        }

        return r.get()
                .filter(user -> passwordEncoder.matches(dto.getPassword(), user.getPasswordHash()))
                .map(user -> {
                    SizToken token = (SizToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    /**
                     * on accroche le user Id à ce token une fois pour toutes.
                     */
                    token.setUserId(user.getId());
                    sizTokenRepository.save(token);
                    return user;
                });
    }
}
