package io.siz.config;

import io.siz.jersey.TokenEndpoint;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(TokenEndpoint.class);
    }
}
