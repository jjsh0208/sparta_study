package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.entity.User;
import com.sparta.myselectshop.repository.FolderRepository;
import com.sparta.myselectshop.repository.ProductFolderRepository;
import com.sparta.myselectshop.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class) // @Mock 사용을 위해 설정합니다.
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    FolderRepository folderRepository;

    @Mock
    ProductFolderRepository productFolderRepository;

    @Mock
    MessageSource messageSource;

    @InjectMocks
    ProductService productService;

    @Test
    @DisplayName("관심 상품 희망가 - 최저가 이상으로 변경")
    void test1() {
        // given
        Long productId = 100L;
        int myprice = ProductService.MIN_MY_PRICE + 3_000_000;

        ProductMypriceRequestDto requestMyPriceDto = new ProductMypriceRequestDto();
        requestMyPriceDto.setMyprice(myprice);

        User user = new User();
        ProductRequestDto requestProductDto = new ProductRequestDto(
                "Apple <b>맥북</b> <b>프로</b> 16형 2021년 <b>M1</b> Max 10코어 실버 (MK1H3KH/A) ",
                "https://shopping-phinf.pstatic.net/main_2941337/29413376619.20220705152340.jpg",
                "https://search.shopping.naver.com/gate.nhn?id=29413376619",
                3515000
        );

        Product product = new Product(requestProductDto, user);



        // 내가 넣어줄 코드 부분을 given에 넣어주고
        // 반환하는 값을 willReturn에 넣어준다.
        // 테스트에 사용할 객체를 생성해 넣어준다.
        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        // when
        ProductResponseDto result = productService.updateProduct(productId, requestMyPriceDto);

        // then
        assertEquals(myprice, result.getMyprice());
    }

    @Test
    @DisplayName("관심 상품 희망가 - 최저가 미만으로 변경")
    void test2() {
        // given
        Long productId = 200L;
        int myprice = ProductService.MIN_MY_PRICE - 50;

        ProductMypriceRequestDto requestMyPriceDto = new ProductMypriceRequestDto();
        requestMyPriceDto.setMyprice(myprice);


        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.updateProduct(productId, requestMyPriceDto);
        });

        // then
        assertEquals(
                "유효하지 않은 관심 가격입니다." +ProductService.MIN_MY_PRICE + "원 이상으로 설정해 주세요.",
                exception.getMessage()
        );
    }
}