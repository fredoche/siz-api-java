package io.siz.security.siz;

import io.siz.repository.siz.SizTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Authenticate a user with a working token.
 */
@Component("SizUserDetailsService")
public class SizUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(SizUserDetailsService.class);

    @Inject
    private SizTokenRepository sizTokenRepository;

    @Override
    /**
     * In that case the username is the token id.
     */
    public UserDetails loadUserByUsername(final String token) {
        log.debug("Authenticating {}", token);
        return sizTokenRepository.findById(token)
                .orElseThrow(() -> new UsernameNotFoundException("Token " + token + " is invalid or expired."));
    }
}
