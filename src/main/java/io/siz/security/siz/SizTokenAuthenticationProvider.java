package io.siz.security.siz;

import javax.inject.Inject;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 *
 * @author fred
 */
@Component
public class SizTokenAuthenticationProvider implements AuthenticationProvider {

    @Inject
    private SizTokenDetailsService sizTokenDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String token = authentication.getCredentials().toString();
            UserDetails details = sizTokenDetailsService.loadUserByUsername(token);

            SizAuthToken sizAuthToken = new SizAuthToken(token, details, details.getAuthorities());
            /**
             * From that point the user is considered to be authenticated.
             */
            sizAuthToken.setAuthenticated(true);

            return sizAuthToken;

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(SizAuthToken.class);
    }
}
