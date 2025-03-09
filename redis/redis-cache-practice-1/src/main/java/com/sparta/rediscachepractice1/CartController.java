package com.sparta.rediscachepractice1;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CartController {

    private static final Logger log = LoggerFactory.getLogger(CartController.class);
    private final CartService cartService;

    @PostMapping
    private CartDto modifyCart(@RequestBody CartItemDto itemDto, HttpSession session){
        cartService.modifyCart(session.getId(),itemDto);
        return  cartService.getCart(session.getId());
    }

    @GetMapping
    public CartDto getCart(HttpSession session){
        log.info(session.getId());
        return cartService.getCart(session.getId());
    }
}
