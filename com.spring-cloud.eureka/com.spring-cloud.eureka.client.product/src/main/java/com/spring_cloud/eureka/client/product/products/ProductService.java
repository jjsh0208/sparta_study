package com.spring_cloud.eureka.client.product.products;

import com.spring_cloud.eureka.client.product.core.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResDto createProduct(ProductReqDto productReqDto, String userId) {
        Product product = Product.createProduct(productReqDto,userId);
        Product savedProduct = productRepository.save(product);
        return  savedProduct.toResDto();
    }

    @Transactional(readOnly = true)
    public Page<ProductResDto> getProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findAllNotDeleted(pageable);

        return productPage.map(Product :: toResDto);
    }

    @Transactional(readOnly = true)
    public ProductResDto getProductDetail(Long productId) {
        Product product = productRepository.findById(productId)
                .filter(p -> p.getDeletedAt() == null)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found or has been deleted"));

       return product.toResDto();

    }

    public ProductResDto updateProduct(Long productId, ProductReqDto productReqDto, String userId) {
        Product product = productRepository.findById(productId)
                .filter(p -> p.getDeletedAt() == null)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found or has been deleted"));

        product.updateProduct(productReqDto,userId);
        Product updatedProduct = productRepository.save(product);

        return updatedProduct.toResDto();
    }

    public void deleteProduct(Long productId, String deleteBy) {
        Product product = productRepository.findById(productId)
                .filter(p -> p.getDeletedAt() == null)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found or has been deleted"));

        product.deletedProduct(deleteBy);
        productRepository.save(product);

    }

    public void reduceProductQuantity(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

        if (product.getQuantity() < quantity){
            throw new IllegalArgumentException("Not enough quantity for product ID: " + productId);
        }

        product.reduceQuantity(quantity);
        productRepository.save(product);
    }

}
