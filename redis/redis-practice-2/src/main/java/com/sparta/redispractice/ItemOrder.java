package com.sparta.redispractice;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("order")
public class ItemOrder {
    @Id
    private String id;
    private String item;
    private Integer count;
    private Long totalPrice;
    private String status;

}
