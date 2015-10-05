package io.siz.service.messaging;

import com.amazonaws.services.sqs.AmazonSQS;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;

/**
 *
 * @author fred
 */
public class SqsQueueSender {

    private final QueueMessagingTemplate queueMessagingTemplate;

    public SqsQueueSender(AmazonSQS amazonSqs) {
        this.queueMessagingTemplate = new QueueMessagingTemplate(amazonSqs);
    }

    public void send(String message) {
        this.queueMessagingTemplate.send("queue.generate.story", MessageBuilder.withPayload(message).build());
    }
}
