package io.siz.config;

import com.amazonaws.services.sqs.AmazonSQS;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.siz.domain.siz.Story;
import io.siz.service.messaging.SqsQueueSender;
import javax.inject.Inject;
import org.springframework.context.annotation.Bean;
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

    private SqsQueueSender sender;

    @Inject
    private ObjectMapper objectMapper;

    @Bean
    public SqsQueueSender sender(AmazonSQS amazonSQS) {
        this.sender = new SqsQueueSender(amazonSQS);
        return sender;
    }

    @ServiceActivator(inputChannel = "videostripCreator")
    public void sendToSqs(Story story) throws JsonProcessingException {
        sender.send(objectMapper.writeValueAsString(story));
    }
}
