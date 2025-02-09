package com.spring_cloud.eureka.client.product.products;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/products")
@EnableFeignClients
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /*
    * 상품 등록
    * path : /api/products
    *
    * Request
    *   header : X-User-Id , X-User-Role
    *   body   : ProductReqDto
    *
    * Response
    *  ProductResDto
    * */

    @PostMapping
    public ResponseEntity<ProductResDto> createProduct(@RequestBody ProductReqDto productReqDto,
                                                       @RequestHeader(value = "X-User-Id",required = true) String userId,
                                                       @RequestHeader(value = "X-Role", required = true) String role){
        if (!"MANAGER".equals(role)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied. User role is not MANAGER.");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.createProduct(productReqDto,userId));
    }


    /*
     * 상품 전체 조회
     * path : /api/products
     *
     * Request
     *   Pageable : 페이지 번호 (page), 페이지 크기 (size), 정렬 기준 (sort) 포함
     *
     * Response
     *  ProductResDto
     * */

    @GetMapping
    public ResponseEntity<Page<ProductResDto>> getProducts(Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getProducts(pageable));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResDto> getProductDetail(@PathVariable("productId")Long productId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getProductDetail(productId));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResDto> updateProduct(@PathVariable("productId")Long productId,
                                                       @RequestBody ProductReqDto productReqDto,
                                                       @RequestHeader(value = "X-User-Id",required = true) String userId,
                                                       @RequestHeader(value = "X-Role", required = true) String role){
        if (!"MANAGER".equals(role)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied. User role is not MANAGER.");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.updateProduct(productId, productReqDto , userId));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable("productId")Long productId,
                                           @RequestHeader(value = "X-User-Id",required = true) String userId,
                                           @RequestHeader(value = "X-Role", required = true) String role){
        if (!"MANAGER".equals(role)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied. User role is not MANAGER.");
        }

        productService.deleteProduct(productId ,userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{productId}/reduceQuantity")
    public ResponseEntity<?> reduceProductQuantity(@PathVariable("productId") Long productId, @RequestParam int quantity){
        productService.reduceProductQuantity(productId, quantity);
        return ResponseEntity.ok().build();
    }

}
