package com.market.order;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    @Value("${message.queue.product}")
    private String productQueue;

    @Value("${message.queue.payment}")
    private String paymentQueue;

    // 래빗 mq로 요청을 보낼때 사용하는 템플릿
    private final RabbitTemplate rabbitTemplate;

    public void createOrder(String orderId){
        rabbitTemplate.convertAndSend(productQueue,orderId);
        rabbitTemplate.convertAndSend(paymentQueue,orderId);
    }
}
