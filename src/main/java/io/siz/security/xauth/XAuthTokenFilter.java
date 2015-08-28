package io.siz.security.xauth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

/**
 * Filters incoming requests and installs a Spring Security principal if a
 * header corresponding to a valid user is found.
 */
public class XAuthTokenFilter extends GenericFilterBean {

    private final Logger log = LoggerFactory.getLogger(XAuthTokenFilter.class);

    /**
     * on injecte la valeur X-Access-Token
     */
    private String headerName = "x-auth-token";

    private final UserDetailsService detailsService;

    private final TokenProvider tokenProvider;

    public XAuthTokenFilter(UserDetailsService detailsService, TokenProvider tokenProvider) {
        this.detailsService = detailsService;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String authToken = httpServletRequest.getHeader(headerName);
            if (StringUtils.hasText(authToken)) {
                String username = tokenProvider.getUserNameFromToken(authToken);
                UserDetails details = detailsService.loadUserByUsername(username);
                if (tokenProvider.validateToken(authToken, details)) {
                    Authentication token = new UsernamePasswordAuthenticationToken(details, details.getPassword(), details.getAuthorities());
                    /**
                     * From that point the user is considered to be
                     * authenticated.
                     */
                    SecurityContextHolder.getContext().setAuthentication(token);
                }
            }
        } catch (Exception ex) {
            log.info("unable to log with xauth filter.");
        } finally {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

}
