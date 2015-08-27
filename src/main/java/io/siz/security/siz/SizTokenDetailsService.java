package io.siz.security.siz;

import io.siz.domain.siz.SizToken;
import io.siz.repository.siz.SizTokenRepository;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Optional;
import org.springframework.security.web.authentication.www.NonceExpiredException;

/**
 * Authenticate a user with a working token.
 */
@Component("sizTokenDetailsService")
public class SizTokenDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(SizTokenDetailsService.class);

    @Inject
    private SizTokenRepository sizTokenRepository;

    @Override
    /**
     * In that case the username is the token id.
     */
    public UserDetails loadUserByUsername(final String token) {
        log.debug("Authenticating {}", token);
        Optional<SizToken> tokenFromDatabase = sizTokenRepository.findById(token);
        return tokenFromDatabase.map(sizToken -> {
            /**
             * Les tokens ne donnent droit qu'au niveau d'authorité USER.
             */
            return new org.springframework.security.core.userdetails.User(
                    sizToken.getId(),
                    ""/*there is no password involved*/,
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        }).orElseThrow(() -> new NonceExpiredException("Token " + token + " is invalid or expired."));
    }
}