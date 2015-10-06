package io.siz.service.messaging;

import com.amazonaws.services.sqs.AmazonSQS;
import io.siz.config.Constants;
import javax.inject.Inject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 *
 * @author fred
 */
@Component
@Profile(Constants.SQS)
@ConfigurationProperties("queue")
public class SqsQueueSender {

    private final QueueMessagingTemplate queueMessagingTemplate;

    private String generateStory;

    @Inject
    public SqsQueueSender(AmazonSQS amazonSqs) {
        this.queueMessagingTemplate = new QueueMessagingTemplate(amazonSqs);
    }

    public void send(String message) {
        queueMessagingTemplate.send(generateStory, MessageBuilder.withPayload(message).build());
    }

    public void setGenerateStory(String generateStory) {
        this.generateStory = generateStory.replace("sqs://", "");
    }
}
