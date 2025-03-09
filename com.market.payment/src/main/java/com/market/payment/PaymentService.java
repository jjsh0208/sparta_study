package com.market.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${message.queue.err.product}")
    private String productQueue;

    public void createPayment(DeliveryMessage deliveryMessage){
        // 결제 금액이 만원이상이면 에러

        Payment payment = Payment.builder()
                .paymentId(UUID.randomUUID())
                .userId(deliveryMessage.getUserId())
                .payAmount(deliveryMessage.getPayAmount())
                .payStatus("SUCCESS")
                .build();

        Integer payAmount = deliveryMessage.getPayAmount();

        if (payment.getPayAmount() >= 10000){
            log.error("payment amount exceeds limit : {}",payAmount);

            deliveryMessage.setErrorType("PAYMENT_LIMIT_EXCEEDED");
            this.rollbackPayment(deliveryMessage);
        }
    }

    public void rollbackPayment(DeliveryMessage deliveryMessage){
        log.info("PAYMENT ROLLBACK !!!");

        rabbitTemplate.convertAndSend(productQueue,deliveryMessage);
    }
}
