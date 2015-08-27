package io.siz.security.siz;

import io.siz.security.siz.SizTokenAuthFilter;
import javax.inject.Inject;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * Configures x-auth-token security. Header can be configured by overriding
 * headerName.
 */
@Component
public class SizAuthConfiguration implements EnvironmentAware {

    private String headerName;

    @Inject
    private UserDetailsService sizTokenDetailsService;

    @Override
    public void setEnvironment(Environment environment) {
        PropertyResolver pr = new RelaxedPropertyResolver(environment, "authentication.siz.");
        headerName = pr.getProperty("headerName", String.class);
    }

    @Bean
    public SizTokenAuthFilter sizTokenAuthFilter() {
        return new SizTokenAuthFilter(sizTokenDetailsService, headerName);
    }
}
