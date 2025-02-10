package com.spring_cloud.eureka.client.order.orders;

import com.spring_cloud.eureka.client.order.core.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT p FROM Order p WHERE p.deletedAt IS NULL")
    Page<Order> findAllNotDeleted(Pageable pageable);
}
