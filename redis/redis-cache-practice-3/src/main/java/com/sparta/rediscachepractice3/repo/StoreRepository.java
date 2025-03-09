package com.sparta.rediscachepractice3.repo;

import com.sparta.rediscachepractice3.domain.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Page<Store> findAllByNameContains(String name, Pageable pageable);
}
