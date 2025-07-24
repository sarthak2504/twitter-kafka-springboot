package com.bingo.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TwitterKafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String topic = "twitter-topic";

    public void sendMessage(String message){
        kafkaTemplate.send(topic, message);
    }

}
