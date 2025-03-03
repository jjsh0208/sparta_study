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

    /**
     * 상품 등록
     * - 관리자만 상품 등록 가능
     *
     * 요청 경로: POST /api/products
     * 요청 헤더:
     *   - X-User-Id: 사용자 ID (필수)
     *   - X-Role: 사용자 역할 (필수, MANAGER만 허용)
     * 요청 본문:
     *   - ProductReqDto: 등록할 상품 정보
     * 응답 본문:
     *   - ProductResDto: 등록된 상품 정보
     * 예외:
     *   - 권한 부족: 403 FORBIDDEN
     */
    @PostMapping
    public ResponseEntity<ProductResDto> createProduct(@RequestBody ProductReqDto productReqDto,
                                                       @RequestHeader(value = "X-User-Id", required = true) String userId,
                                                       @RequestHeader(value = "X-Role", required = true) String role) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.createProduct(productReqDto, userId , role));
    }

    /**
     * 상품 전체 조회
     *
     * 요청 경로: GET /api/products
     * 요청 매개변수:
     *   - Pageable: 페이지 번호(page), 페이지 크기(size), 정렬 기준(sort) 포함
     * 응답 본문:
     *   - Page<ProductResDto>: 페이징 처리된 상품 목록
     */
    @GetMapping
    public ResponseEntity<Page<ProductResDto>> getProducts(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getProducts(pageable));
    }

    /**
     * 상품 상세 조회
     *
     * 요청 경로: GET /api/products/{productId}
     * 경로 매개변수:
     *   - productId: 조회하려는 상품의 ID
     * 응답 본문:
     *   - ProductResDto: 해당 상품의 상세 정보
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResDto> getProductDetail(@PathVariable("productId") Long productId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getProductDetail(productId));
    }

    /**
     * 상품 수정
     * - 관리자만 상품 수정 가능
     *
     * 요청 경로: PUT /api/products/{productId}
     * 경로 매개변수:
     *   - productId: 수정하려는 상품의 ID
     * 요청 헤더:
     *   - X-User-Id: 사용자 ID (필수)
     *   - X-Role: 사용자 역할 (필수, MANAGER만 허용)
     * 요청 본문:
     *   - ProductReqDto: 수정할 상품 정보
     * 응답 본문:
     *   - ProductResDto: 수정된 상품 정보
     * 예외:
     *   - 권한 부족: 403 FORBIDDEN
     */
    @PutMapping("/{productId}")
    public ResponseEntity<ProductResDto> updateProduct(@PathVariable("productId") Long productId,
                                                       @RequestBody ProductReqDto productReqDto,
                                                       @RequestHeader(value = "X-User-Id", required = true) String userId,
                                                       @RequestHeader(value = "X-Role", required = true) String role) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.updateProduct(productId, productReqDto, userId, role));
    }

    /**
     * 상품 삭제
     * - 관리자만 상품 삭제 가능
     *
     * 요청 경로: DELETE /api/products/{productId}
     * 경로 매개변수:
     *   - productId: 삭제하려는 상품의 ID
     * 요청 헤더:
     *   - X-User-Id: 사용자 ID (필수)
     *   - X-Role: 사용자 역할 (필수, MANAGER만 허용)
     * 응답 본문:
     *   - 없음 (HTTP 200 OK)
     * 예외:
     *   - 권한 부족: 403 FORBIDDEN
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable("productId") Long productId,
                                           @RequestHeader(value = "X-User-Id", required = true) String userId,
                                           @RequestHeader(value = "X-Role", required = true) String role) {

        productService.deleteProduct(productId, userId, role);
        return ResponseEntity.ok().build();
    }

    /**
     * 상품 재고 감소
     *
     * 요청 경로: GET /api/products/{productId}/reduceQuantity
     * 경로 매개변수:
     *   - productId: 재고를 감소시킬 상품의 ID
     * 요청 매개변수:
     *   - quantity: 감소시킬 수량 (정수값)
     * 응답 본문:
     *   - 없음 (HTTP 200 OK)
     */
    @GetMapping("/{productId}/reduceQuantity")
    public ResponseEntity<?> reduceProductQuantity(@PathVariable("productId") Long productId, @RequestParam int quantity) {
        productService.reduceProductQuantity(productId, quantity);
        return ResponseEntity.ok().build();
    }



}
