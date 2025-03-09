package com.market.order;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderEndPoint {

    private final OrderService orderService;


    @RabbitListener(queues = "${message.queue.err.order}")
    private void errorOrder(DeliveryMessage deliveryMessage){
        log.info("ERROR RECEIVE !!!");
        orderService.rollbackOrder(deliveryMessage);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable("orderId") UUID orderId){
        Order order = orderService.getOrder(orderId);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/order")
    public ResponseEntity<Order> order(@RequestBody OrderReqDto orderReqDto){
        Order order = orderService.createOrder(orderReqDto);
        return ResponseEntity.ok(order);
    }


    @Data
    public static class OrderReqDto {
        private String userId;
        private Integer productId;
        private Integer productQuantity;
        private Integer payAmount;

        public Order toOrder() {
            return Order.builder()
                    .orderId(UUID.randomUUID())
                    .userId(userId)
                    .orderStatus("RECEIPT")
                    .build();
        }

        public DeliveryMessage toDeliveryMessage(UUID orderId) {
            return DeliveryMessage.builder()
                    .orderId(orderId)
                    .userId(userId)
                    .productId(productId)
                    .productQuantity(productQuantity)
                    .payAmount(payAmount)
                    .build();
        }
    }

}
