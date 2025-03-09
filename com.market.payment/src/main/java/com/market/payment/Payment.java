package com.market.payment;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Payment {

    private UUID paymentId;

    private String userId;

    private Integer payAmount;

    private String payStatus; // 에러 사유 저장용


}
