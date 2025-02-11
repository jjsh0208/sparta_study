package com.spring_cloud.eureka.client.order.orders;

import com.spring_cloud.eureka.client.order.core.client.ProductClient;
import com.spring_cloud.eureka.client.order.core.domain.Order;
import com.spring_cloud.eureka.client.order.core.enums.OrderStatus;
import com.spring_cloud.eureka.client.product.products.ProductResDto;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    public OrderResDto createOrder(OrderReqDto orderReqDto, String userId) {
            Order order = Order.createOrder(userId);

            for (Map.Entry<Long,Integer> entry : orderReqDto.getOrderProductIds().entrySet()){
                Long productId = entry.getKey();
                Integer quantity = entry.getValue();

                ProductResDto product = productClient.getProductDetail(productId).getBody();

                if (product == null || product.getQuantity() < 1 ){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "product is sold out");
                }

                productClient.reduceProductQuantity(productId,quantity);

                order.addOrderProducts(productId);
            }

            return orderRepository.save(order).toResDto();


    }

    @Transactional(readOnly = true)
    public Page<OrderResDto> getOrders(Pageable pageable) {
        Page<Order> orderPages = orderRepository.findAllNotDeleted(pageable);

        return orderPages.map(Order::toResDto);
    }

    @Transactional(readOnly = true)
    public OrderResDto getOrderDetail(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .filter(p -> p.getDeletedAt() == null)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        return order.toResDto();
    }

    public OrderResDto updateOrder(Long orderId, OrderReqDto orderReqDto, String userId, String role) {
        Order order = orderRepository.findById(orderId)
                .filter(p -> p.getDeletedAt() == null)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        if (!order.getCreateBy().equals(userId) || !"MANAGER".equals(role)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied. User role is not MANAGER.");
        }

        List<Long> newOrder = new ArrayList<>();

        for (Map.Entry<Long,Integer> entry : orderReqDto.getOrderProductIds().entrySet()){
            Long productId = entry.getKey();
            Integer quantity = entry.getValue();

            ProductResDto product = productClient.getProductDetail(productId).getBody();

            if (product == null || product.getQuantity() < 1 ){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "product is sold out");
            }

            productClient.reduceProductQuantity(productId,quantity);

            newOrder.add(productId);
        }
        order.updateOrder(newOrder,userId, OrderStatus.CREATED);

        return orderRepository.save(order).toResDto();
    }

    public void deleteOrder(Long orderId, String userId, String role) {
        Order order = orderRepository.findById(orderId)
                .filter(p -> p.getDeletedAt() == null)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        if (!order.getCreateBy().equals(userId) || !"MANAGER".equals(role)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied. User role is not MANAGER.");
        }

        order.deleteOrder(userId);
        orderRepository.save(order);
    }
}
