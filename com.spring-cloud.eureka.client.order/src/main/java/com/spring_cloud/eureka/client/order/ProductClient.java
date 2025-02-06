package com.spring_cloud.eureka.client.order;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service") //product 서버의 이름
public interface ProductClient {

    @GetMapping("product/{productId}")
    String getProduct(@PathVariable String productId); // product 서버에서 호출할 url 와 메서드 동일하게 작성해줘야함
}
