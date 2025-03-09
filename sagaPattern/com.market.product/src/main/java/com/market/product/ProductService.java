package com.market.product;

import ch.qos.logback.core.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${message.queue.payment}")
    private String paymentQueue;

    @Value("${message.queue.err.order}")
    private String errorOrderQueue;

    public void reduceProductAmount(DeliveryMessage deliveryMessage){
        Integer productId = deliveryMessage.getProductId();

        Integer productQuantity = deliveryMessage.getProductQuantity();

        if(productId != 1 || productQuantity > 1){
            this.rollbackProduct(deliveryMessage);
            return;
        }

        rabbitTemplate.convertAndSend(paymentQueue,deliveryMessage);

    }

    public void rollbackProduct(DeliveryMessage deliveryMessage) {
        log.info("PRODUCT ROLLBACK");

        if (!StringUtils.hasText(deliveryMessage.getErrorType())){ // 에러 타입이 존재하지않으면 product에서 발생한 에러
            deliveryMessage.setErrorType("PRODUCT ERROR");
            System.out.println("test  : " + deliveryMessage.getErrorType());
        }
        rabbitTemplate.convertAndSend(errorOrderQueue, deliveryMessage);
    }
}
