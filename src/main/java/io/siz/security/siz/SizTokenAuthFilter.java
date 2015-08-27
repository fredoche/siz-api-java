package io.siz.security.siz;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

/**
 * Filters incoming requests and installs a Spring Security principal if a
 * header corresponding to a valid user is found.
 */
public class SizTokenAuthFilter extends GenericFilterBean {

    /**
     * on injecte la valeur X-Access-Token
     */
    private final String headerName;

    private final UserDetailsService sizTokenDetailsService;

    public SizTokenAuthFilter(UserDetailsService sizTokenDetailsService, String headerName) {
        this.sizTokenDetailsService = sizTokenDetailsService;
        this.headerName = headerName;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String authToken = httpServletRequest.getHeader(headerName);
            if (StringUtils.hasText(authToken)) {
                UserDetails details = sizTokenDetailsService.loadUserByUsername(authToken);
                Authentication token = new AnonymousAuthenticationToken(details.getUsername(), details, details.getAuthorities());
                /**
                 * From that point the user is considered to be authenticated.
                 */
                SecurityContextHolder.getContext().setAuthentication(token);
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
