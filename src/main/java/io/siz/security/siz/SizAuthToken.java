package io.siz.security.siz;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Structure intermédiaire pour d'abord stocker les information d'identité, puis
 * ensuite être testé pour valider l'authentification.
 *
 * @author fred
 */
public class SizAuthToken extends AbstractAuthenticationToken {

    private final String token;

    private UserDetails principal;

    public SizAuthToken(String token, UserDetails principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.token = token;
        this.principal = principal;
    }

    @Override
    public String getCredentials() {
        return token;
    }

    @Override
    public UserDetails getPrincipal() {
        return principal;
    }

    public void setPrincipal(UserDetails sizToken) {
        this.principal = sizToken;
    }
}
