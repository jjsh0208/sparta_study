package com.market.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${message.queue.product}")
    private String productQueue;

    private Map<UUID,Order> orderStore = new HashMap<>();

    public Order createOrder(OrderEndPoint.OrderReqDto orderReqDto){
        Order order = orderReqDto.toOrder();

        orderStore.put(order.getOrderId(),order);

        // 어떤 오더에서 발생했는지 알려주기 위해서 order를 같이 보내준다.
        DeliveryMessage deliveryMessage = orderReqDto.toDeliveryMessage(order.getOrderId());

        rabbitTemplate.convertAndSend(productQueue, deliveryMessage);

        return order;
    }

    public Order getOrder(UUID orderId) {
        return orderStore.get(orderId);
    }

    public void rollbackOrder(DeliveryMessage deliveryMessage) {
        Order order = orderStore.get(deliveryMessage.getOrderId());
        order.cancelOrder(deliveryMessage.getErrorType());
    }
}
