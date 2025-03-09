package com.market.product;

import lombok.*;

import java.util.UUID;

@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryMessage {
    private UUID orderId;

    private UUID paymentId;

    private String userId;

    private Integer productId;

    private Integer productQuantity;

    private Integer payAmount;

    private String errorType;
}
