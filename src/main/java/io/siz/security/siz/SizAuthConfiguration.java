package io.siz.security.siz;

import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;

/**
 * Configures x-auth-token security. Header can be configured by overriding
 * headerName.
 * @deprecated en attendant de savoir si on en a besoin
 */
//@Configuration
public class SizAuthConfiguration implements EnvironmentAware {

    private String headerName;

    @Override
    public void setEnvironment(Environment environment) {
        PropertyResolver pr = new RelaxedPropertyResolver(environment, "authentication.siz.");
        headerName = pr.getProperty("headerName", String.class);
    }
}
