package com.spring_cloud.eureka.client.order.core.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.awt.print.Pageable;
import com.spring_cloud.eureka.client.product.products.ProductResDto;

@FeignClient(name = "product-service") //product 서버의 이름
public interface ProductClient {

    // product 상품 단일 조회
    @GetMapping("/api/products/{productId}")
    ResponseEntity<ProductResDto> getProductDetail(@PathVariable("productId")Long productId);

    // product 상품 수량 조정
    @GetMapping("/api/products/{productId}/reduceQuantity")
    ResponseEntity<?> reduceProductQuantity(@PathVariable("productId") Long productId, @RequestParam int quantity);
}



