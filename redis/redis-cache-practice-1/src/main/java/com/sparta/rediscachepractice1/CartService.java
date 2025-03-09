package com.sparta.rediscachepractice1;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final String keyString = "cart:%s";
    private final RedisTemplate<String,String> cartTemplate;
    private final HashOperations<String,String,Integer> hashOps;

    public void modifyCart(String id, CartItemDto itemDto) {
        hashOps.increment(
                keyString.formatted(id),
                itemDto.getItem(),
                itemDto.getCount()
        );

        int count = Optional.ofNullable(hashOps.get(keyString.formatted(id),itemDto.getItem()))
                .orElse(0);
        if (count <= 0){
            hashOps.delete(keyString.formatted(id),itemDto.getItem());
        }
    }

    public CartDto getCart(String id) {
        boolean exists =Optional.ofNullable(cartTemplate.hasKey(keyString.formatted(id)))
                .orElse(false);
        if (!exists)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        // 현재 시간에서 3시간을 더함
        Date expireAt = Date.from(Instant.now().plus(30, ChronoUnit.SECONDS));
        cartTemplate.expireAt(
                keyString.formatted(id),
                expireAt
        );

        return CartDto.fromHashPairs(
                hashOps.entries(keyString.formatted(id)),
                expireAt
        );
    }
}
