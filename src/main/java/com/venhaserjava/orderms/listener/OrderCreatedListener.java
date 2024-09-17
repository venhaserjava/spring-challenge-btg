package com.venhaserjava.orderms.listener;

//import org.springframework.amqp.core.Message;
import org.springframework.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.venhaserjava.orderms.listener.dto.OrderCreatedEvent;

import static com.venhaserjava.orderms.config.RabbitMqConfig.ORDER_CREATED_QUEUE;

@Component
public class OrderCreatedListener {

    private final Logger logger = LoggerFactory.getLogger(OrderCreatedListener.class)        ;

    @RabbitListener(queues = ORDER_CREATED_QUEUE)
    public void Listen(Message<OrderCreatedEvent> message){
        logger.info("Message Consumed: {}", message);
    }
}
