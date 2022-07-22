package com.example.crudapplicationsql.rmq;

import com.example.crudapplicationsql.entity.Book;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RmqMessageProducer {
    @Value("${exchange.name}")
    private String exchangeName;

    @Value("${routingKey.name}")
    private String routingKey;

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendBookDetailsToQueue(Book book){
        rabbitTemplate.convertAndSend(exchangeName, routingKey, book);
    }
}
