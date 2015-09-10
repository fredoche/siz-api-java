package io.siz.security.siz;

import org.springframework.util.StringUtils;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Notre filtre maison qui se charge de positionner un User dans le contexte de
 * sécurité.
 *
 * @author fred
 */
public class SizAuthTokenFilter extends GenericFilterBean {

    private final Logger log = LoggerFactory.getLogger(SizAuthTokenFilter.class);
    /**
     * on injecte la valeur X-Access-Token
     */
    private final String headerName;

    private final SizUserDetailsService sizTokenDetailsService;

    public SizAuthTokenFilter(String headerName, SizUserDetailsService sizTokenDetailsService) {
        this.headerName = headerName;
        this.sizTokenDetailsService = sizTokenDetailsService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String authToken = httpServletRequest.getHeader(headerName);
            if (StringUtils.hasText(authToken)) {
                UserDetails details = sizTokenDetailsService.loadUserByUsername(authToken);

                /**
                 * From that point the user is considered to be authenticated.
                 */
                SecurityContextHolder.getContext()
                        .setAuthentication(
                                new UsernamePasswordAuthenticationToken(details, authToken, details.getAuthorities())
                        );

            }
        } catch (Exception ex) {
            log.info("Could not log with siz credentials.", ex.getMessage());
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
