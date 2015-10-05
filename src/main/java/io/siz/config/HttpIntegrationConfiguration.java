package io.siz.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.EnablePublisher;

@Configuration
@Profile("!" + Constants.SQS)
@EnableIntegration
@ImportResource("integration/integration-context.xml")
@EnablePublisher("defaultPublisherChannel")
public class HttpIntegrationConfiguration {

    /**
     * only load int-http conf if not using sqs.
     */
}
