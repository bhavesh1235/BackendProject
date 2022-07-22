package com.example.crudapplicationsql.kafka;

import com.example.crudapplicationsql.entity.Book;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageProducer {
    @Value("${kafka_topic}")
    private String kafkaTopic;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendBookDetailsToTopic(Book book) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            kafkaTemplate.send(kafkaTopic,objectMapper.writeValueAsString(book));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
