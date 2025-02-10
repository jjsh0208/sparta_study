package com.spring_cloud.eureka.client.order.orders;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderReqDto {

    private Map<Long,Integer> orderProductIds;
    private String status;
}
