package com.spring_cloud.eureka.client.product.core.domain;

import com.spring_cloud.eureka.client.product.products.ProductReqDto;
import com.spring_cloud.eureka.client.product.products.ProductResDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;
    private String description;
    private Integer price;
    private Integer quantity;

    private LocalDateTime createdAt;
    private String createBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private LocalDateTime deletedAt;
    private String deletedBy;

    // 생성일
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // 업데이트
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public static Product createProduct(ProductReqDto productReqDto, String userId) {
        return Product.builder()
                .name(productReqDto.getName())
                .description(productReqDto.getDescription())
                .price(productReqDto.getPrice())
                .quantity(productReqDto.getQuantity())
                .createBy(userId)
                .build();
    }

    /*
    * PUT 업데이트
    * */
    public void updateProduct(ProductReqDto productReqDto, String userId) {
        this.name = productReqDto.getName();
        this.description = productReqDto.getDescription();
        this.price = productReqDto.getPrice();
        this.quantity = productReqDto.getQuantity();
        this.updatedBy = userId;
        this.updatedAt = LocalDateTime.now();
    }

    public void deletedProduct(String deletedBy) {
        this.deletedBy = deletedBy;
        this.deletedAt = LocalDateTime.now();
    }

    /*
     * DTO로 변환하는 메서드
     * */
    public ProductResDto toResDto() {
        return new ProductResDto(
                this.id,
                this.name,
                this.description,
                this.price,
                this.quantity,
                this.createdAt,
                this.createBy,
                this.updatedAt,
                this.updatedBy
        );
    }

    public void reduceQuantity(int i){
        this.quantity = this.quantity - i;
    }

}
