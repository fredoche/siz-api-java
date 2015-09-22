package io.siz.service.siz;

import io.siz.domain.siz.SizToken;
import io.siz.domain.siz.SizUser;
import io.siz.exception.SizException;
import io.siz.repository.siz.SizTokenRepository;
import io.siz.repository.siz.SizUserRepository;
import io.siz.web.rest.dto.siz.SizUserDTO;
import java.util.Date;
import java.util.Optional;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.facebook.api.User;
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
            log.info("Creating user");
            SizUser user = new SizUser();

            userDTO.getUsername().ifPresent(user::setUsername);
            userDTO.getPassword().map(passwordEncoder::encode).ifPresent(user::setPasswordHash);
            user.setCreationDate(new Date());
            // create by email
            userDTO.getEmail().ifPresent(user::setEmail);

            // create by facebook
            userDTO.getFacebookToken()
                    .map(this::getUserProfile)
                    .ifPresent(userProfile -> {
                        log.info("Creating user by facebook");
                        /**
                         * on teste la verif de l'email.
                         */
                        user.setEmail(userProfile.getEmail());
                        /**
                         * mais on veut surtout garder son facebookUserId.
                         */
                        user.setFacebookUserId(userProfile.getId());
                    }
                    );

            SizUser insertedUser = sizUserRepository.insert(user);
            token.setUserId(insertedUser.getId());
            sizTokenRepository.save(token);
            return user;
        }
    }

    private User getUserProfile(String facebookToken) {
        return new FacebookTemplate(facebookToken).userOperations().getUserProfile();
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

        Optional<SizUser> oUser;
        if (dto.getEmail().isPresent() && dto.getPassword().isPresent()) {
            oUser = dto.getEmail()
                    .flatMap(sizUserRepository::findByEmail)
                    .filter(user -> passwordEncoder.matches(dto.getPassword().get(), user.getPasswordHash()));

        } else if (dto.getUsername().isPresent() && dto.getPassword().isPresent()) {
            oUser = dto.getUsername()
                    .flatMap(sizUserRepository::findByUsername)
                    .filter(user -> passwordEncoder.matches(dto.getPassword().get(), user.getPasswordHash()));

        } else if (dto.getFacebookToken().isPresent()) {
            oUser = dto.getFacebookToken()
                    .map(this::getUserProfile)
                    .map(User::getId)
                    .flatMap(sizUserRepository::findByFacebookUserId);
        } else {
            throw new SizException("provide a valid login tuple.");
        }

        return oUser.map(user -> {
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
