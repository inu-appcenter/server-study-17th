package com.blog.appcenter_server_week2.domain.repository;

import com.blog.appcenter_server_week2.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product, Long>{
    Optional<Product> findByPostIdAndUserId(Long postId, Long userId);
}
