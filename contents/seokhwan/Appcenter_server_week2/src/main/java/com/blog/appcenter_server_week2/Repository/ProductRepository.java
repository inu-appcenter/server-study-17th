package com.blog.appcenter_server_week2.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.blog.appcenter_server_week2.Entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>{

}
