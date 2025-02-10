package com.spring_cloud.eureka.client.order.orders;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResDto> createOrder(@RequestBody OrderReqDto orderReqDto,
                                                   @RequestHeader(value = "X-User-Id",required = true) String userId){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(orderService.createOrder(orderReqDto , userId));
    }

    @GetMapping
    public ResponseEntity<Page<OrderResDto>> getOrderDetails(Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.getOrders(pageable));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResDto> getOrderDetail(@PathVariable("orderId") Long orderId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.getOrderDetail(orderId));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResDto> updateOrder(@PathVariable("orderId") Long orderId,
                                                   @RequestBody OrderReqDto orderReqDto,
                                                   @RequestHeader(value = "X-User-Id",required = true) String userId,
                                                   @RequestHeader(value = "X-Role", required = true) String role){

        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.updateOrder(orderId, orderReqDto, userId, role));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable("orderId") Long orderId,
                                         @RequestHeader(value = "X-User-Id",required = true) String userId,
                                         @RequestHeader(value = "X-Role", required = true) String role){
        orderService.deleteOder(orderId ,userId, role);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
