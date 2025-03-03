package com.spring_cloud.eureka.client.order.core.domain;

import com.spring_cloud.eureka.client.order.core.enums.OrderStatus;
import com.spring_cloud.eureka.client.order.orders.OrderResDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long order_id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime createdAt;
    private String createBy;

    private LocalDateTime updatedAt;
    private String updatedBy;

    private LocalDateTime deletedAt;
    private String deletedBy;

    @ElementCollection
    private List<Long> orderProducts;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // 업데이트
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public static Order createOrder(String createBy){
        return Order.builder()
                .createBy(createBy)
                .status(OrderStatus.CREATED)
                .orderProducts(new ArrayList<>())
                .build();
    }

    public void addOrderProducts(Long productId){
        this.orderProducts.add(productId);
    }

    public void updateStatus(OrderStatus status){
        this.status = status;
    }

    public void updateOrder(List<Long> orderItems, String updatedBy, OrderStatus status){
        this.orderProducts = orderItems;
        this.updatedBy = updatedBy;
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public void deleteOrder(String deletedBy){
        this.deletedBy = deletedBy;
        this.deletedAt = LocalDateTime.now();
    }

    public OrderResDto toResDto(){
        return new OrderResDto(
                this.order_id,
                this.status.name(),
                this.createdAt,
                this.createBy,
                this.updatedAt,
                this.updatedBy,
                this.orderProducts
        );
    }
}
