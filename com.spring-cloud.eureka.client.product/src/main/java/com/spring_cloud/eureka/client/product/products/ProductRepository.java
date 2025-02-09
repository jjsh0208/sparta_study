package com.spring_cloud.eureka.client.product.products;

import com.spring_cloud.eureka.client.product.core.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p from Product p WHERE p.deletedAt IS NULL")
    Page<Product> findAllNotDeleted(Pageable pageable);
}
