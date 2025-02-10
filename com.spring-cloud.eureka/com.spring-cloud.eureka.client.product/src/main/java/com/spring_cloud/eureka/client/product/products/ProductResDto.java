package com.spring_cloud.eureka.client.product.products;

import com.spring_cloud.eureka.client.product.core.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResDto {
    private Long id;
    private String name;
    private String description;
    private Integer price;
    private Integer quantity;

    private LocalDateTime createdAt;
    private String createBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

    public Product fromResDto( String userId) {
        return Product.builder()
                .product_id(this.getId())
                .name(this.name)
                .description(this.description)
                .price(this.price)
                .quantity(this.quantity)
                .createBy(userId) // userId는 이 값을 넣어야 할 것 같아서 예시로 추가
                .build();
    }

}
