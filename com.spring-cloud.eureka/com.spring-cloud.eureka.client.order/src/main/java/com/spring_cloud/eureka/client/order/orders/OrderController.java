package com.spring_cloud.eureka.client.order.orders;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    /**
     *  주문 등록
     *  - 주문을 생성하는 API입니다.
     *  - 재고가 부족한 경우 예외 발생
     *
     *  요청 경로 : POST /api/orders
     *  요청 헤더 :
     *      - X-User-Id : 사용자 ID (필수)
     *  요청 본문:
     *      - OrderReqDto : 등록할 주문 정보
     *  응답 본문:
     *      - OrderResDto : 등록된 주문 정보
     *  예외 :
     *      - 재고 소진 : 404 NOT FOUND
     */
    @PostMapping
    public ResponseEntity<OrderResDto> createOrder(@RequestBody OrderReqDto orderReqDto,
                                                   @RequestHeader(value = "X-User-Id", required = true) String userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.createOrder(orderReqDto, userId));
    }

    /**
     *  주문 전체 조회
     *  - 주문 목록을 페이징 처리하여 조회합니다.
     *  - 페이지 번호(page), 페이지 크기(size), 정렬 기준(sort)을 포함한 요청을 받습니다.
     *
     *  요청 경로 : GET /api/orders
     *  요청 매개변수:
     *      - Pageable : 페이지 번호 (page), 페이지 크기 (size), 정렬 기준(sort) 포함
     *  응답 본문:
     *      - OrderResDto : 페이지 처리된 주문 목록
     */
    @GetMapping
    public ResponseEntity<Page<OrderResDto>> getOrderDetails(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.getOrders(pageable));
    }

    /**
     *  주문 상세 조회
     *  - 특정 주문의 상세 정보를 조회합니다.
     *  - 요청된 주문이 존재하지 않거나 삭제된 경우 예외 발생
     *
     *  요청 경로 : GET /api/orders/{orderId}
     *  경로 매개변수 :
     *      - orderId : 조회할 주문의 ID
     *  응답 본문:
     *      - OrderResDto : 해당 주문의 상세 정보
     *  예외 :
     *      - 주문이 존재하지 않거나 삭제된 경우 : 404 NOT FOUND
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResDto> getOrderDetail(@PathVariable("orderId") Long orderId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.getOrderDetail(orderId));
    }

    /**
     *  주문 수정
     *  - 주문 작성자나 관리자만 주문을 수정할 수 있습니다.
     *  - 요청된 주문이 존재하지 않거나 삭제된 경우 예외 발생
     *
     *  요청 경로 : PUT /api/orders/{orderId}
     *  경로 매개변수 :
     *      - orderId : 수정하려는 주문의 ID
     *  요청 헤더 :
     *      - X-User-Id : 사용자 ID (필수)
     *      - X-Role : 사용자 권한 (필수, MANAGER인 경우에만 수정 가능)
     *  요청 본문:
     *      - OrderReqDto : 수정할 주문 정보
     *  응답 본문:
     *      - OrderResDto : 수정된 주문 정보
     *  예외 :
     *      - 주문이 존재하지 않거나 삭제된 경우 : 404 NOT FOUND
     *      - 권한 부족 : 403 FORBIDDEN
     */
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResDto> updateOrder(@PathVariable("orderId") Long orderId,
                                                   @RequestBody OrderReqDto orderReqDto,
                                                   @RequestHeader(value = "X-User-Id", required = true) String userId,
                                                   @RequestHeader(value = "X-Role", required = true) String role) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.updateOrder(orderId, orderReqDto, userId, role));
    }

    /**
     *  주문 삭제
     *  - 주문 작성자나 관리자만 주문을 삭제할 수 있습니다.
     *  - 삭제된 주문은 더 이상 조회나 수정할 수 없습니다.
     *
     *  요청 경로 : DELETE /api/orders/{orderId}
     *  경로 매개변수 :
     *      - orderId : 삭제하려는 주문의 ID
     *  요청 헤더 :
     *      - X-User-Id : 사용자 ID (필수)
     *      - X-Role : 사용자 권한 (필수, MANAGER인 경우에만 삭제 가능)
     *  예외 :
     *      - 주문이 존재하지 않거나 삭제된 경우 : 404 NOT FOUND
     *      - 권한 부족 : 403 FORBIDDEN
     */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable("orderId") Long orderId,
                                         @RequestHeader(value = "X-User-Id", required = true) String userId,
                                         @RequestHeader(value = "X-Role", required = true) String role) {
        orderService.deleteOrder(orderId, userId, role);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
