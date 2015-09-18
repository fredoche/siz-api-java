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
    private SizTokenService sizTokenService;
    @Inject
    private PasswordEncoder passwordEncoder;

    @PreAuthorize("hasRole('ROLE_USER')")
    public SizUser create(SizUserDTO userDTO) {
        // 1 on récupère le token complet du type.
        SizToken token = sizTokenService.getCurrentToken();
        // 2 on regarde si ce token est déja accroché à un user
        if (!isEmpty(token.getUserId())) {
            final String m = "A user is already connected with this token";
            log.error(m);
            throw new SizException(m);
        } else {
            SizUser user = new SizUser();

            user.setUsername(userDTO.getUsername());
            user.setPasswordHash(passwordEncoder.encode(userDTO.getPassword()));
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
                 * mais on veut surtout garder son facebookUserId.
                 */
                user.setFacebookUserId(user.getFacebookUserId());
            }

            try {
                user = sizUserRepository.insert(user);
                token.setUserId(user.getId());
                sizTokenRepository.save(token);
                return user;

            } catch (Exception e) {
                log.error("exception caught : {}", e.getMessage());
                throw e;
            }
        }
    }

    /**
     * Le Principal est positionné là dans le {@link io.siz.security.siz.SizAuthTokenFilter} et il s'agit d'un
     * {@link io.siz.domain.siz.SizToken}
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
            r = () -> sizUserRepository.findByFacebookUserId(dto.getFacebookToken());
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
