package com.sparta.rediscachepractice1;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    private String item;
    private Integer count;
}
