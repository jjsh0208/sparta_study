package com.spring_cloud.eureka.client.order.orders;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResDto {
    private Long order_id;

    private String status;

    private LocalDateTime createdAt;
    private String createBy;

    private LocalDateTime updatedAt;
    private String updatedBy;

    private List<Long> orderProducts = new ArrayList<>();
}
