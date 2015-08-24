package io.siz.config;

import io.siz.security.xauth.TokenProvider;
import io.siz.security.xauth.XAuthTokenFilter;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * Configures x-auth-token security. Header can be configured by overriding
 * headerName.
 */
@Component
public class XAuthConfiguration implements EnvironmentAware {

    private String secret;
    private int tokenValidityInSeconds;
    private String headerName;

    @Override
    public void setEnvironment(Environment environment) {
        PropertyResolver pr = new RelaxedPropertyResolver(environment, "authentication.xauth.");
        secret = pr.getProperty("secret", String.class);
        tokenValidityInSeconds = pr.getProperty("tokenValidityInSeconds", Integer.class);
        headerName = pr.getProperty("headerName", String.class);
    }

    @Bean
    public TokenProvider tokenProvider() {
        final TokenProvider tokenProvider = new TokenProvider();
        tokenProvider.setSecretKey(secret);
        tokenProvider.setTokenValidity(tokenValidityInSeconds);
        return tokenProvider;
    }

    @Bean
    public XAuthTokenFilter xAuthTokenFilter(UserDetailsService userDetailsService, TokenProvider tokenProvider) {
        final XAuthTokenFilter xAuthTokenFilter = new XAuthTokenFilter(userDetailsService, tokenProvider);
        xAuthTokenFilter.setHeaderName(headerName);
        return xAuthTokenFilter;
    }
    
//    @Bean 
//    public AnonymousAuthenticationFilter anonymousAuthenticationFilter() {
//        return new AnonymousAuthenticationFilter();
//    }
}
