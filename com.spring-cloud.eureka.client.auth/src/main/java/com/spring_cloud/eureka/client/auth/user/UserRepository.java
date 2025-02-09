package com.spring_cloud.eureka.client.auth.user;

import com.spring_cloud.eureka.client.auth.core.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);

}
