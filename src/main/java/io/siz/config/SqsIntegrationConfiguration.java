package io.siz.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.siz.domain.siz.Story;
import io.siz.service.messaging.SqsQueueSender;
import javax.inject.Inject;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.EnablePublisher;

/**
 *
 * @author fred
 */
@Configuration
@MessageEndpoint
@EnableIntegration
@Profile(Constants.SQS)
@EnablePublisher("defaultPublisherChannel")
public class SqsIntegrationConfiguration {

    @Inject
    private SqsQueueSender sender;

    @Inject
    private ObjectMapper objectMapper;

    @ServiceActivator(inputChannel = "videostripChannel")
    public void sendToSqs(Story story) throws JsonProcessingException {
        sender.send(objectMapper.writeValueAsString(story));
    }
}
