package com.blog.appcenter_server_week2.domain.repository;

import com.blog.appcenter_server_week2.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long>{

}
