package com.sparta.rediscachepractice3.domain;

import lombok.*;

import java.io.Serializable;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreDto implements Serializable {
    private Long id;
    private String name;
    private String category;


    public static StoreDto formEntity(Store entity){
            return StoreDto.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .category(entity.getCategory())
                    .build();
    }

}
