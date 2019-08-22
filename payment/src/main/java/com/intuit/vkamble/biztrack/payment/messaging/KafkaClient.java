package com.intuit.vkamble.biztrack.payment.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaClient {

    private static Logger log = LoggerFactory.getLogger(KafkaClient.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaClient(KafkaTemplate<String, String> kafkaTemplate) {

        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topicName, String msg) {
        kafkaTemplate.send(topicName, msg);
    }
}
