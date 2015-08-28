package io.siz.security.siz;

import org.springframework.util.StringUtils;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class SizAuthTokenFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * on injecte la valeur X-Access-Token
     */
    private final String headerName;

    public SizAuthTokenFilter(String headerName) {
        super("/*");// allow any request to contain an authorization header
        this.headerName = headerName;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String authToken = request.getHeader(headerName);
        if (StringUtils.hasText(authToken)) {
            return new SizAuthToken(authToken, null, null);
        }
        /**
         * pas d'en-tête, on laisse faire les autres filtres.
         */
        return null;
    }

}
