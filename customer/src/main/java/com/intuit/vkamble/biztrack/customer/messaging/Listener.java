package com.intuit.vkamble.biztrack.customer.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Listener {

    private static Logger log = LoggerFactory.getLogger(Listener.class);

    @KafkaListener(topics = KafkaConstants.PAYMENT_TOPIC, groupId = KafkaConstants.GROUP_ID_CONFIG)
    public void listen(String message) {
        log.info("Payment received:{}",message);
    }
}
