package com.spring_cloud.eureka.client.product.products;


import com.spring_cloud.eureka.client.product.core.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductReqDto {
    private String name;
    private String description;
    private Integer price;
    private Integer quantity;
}
