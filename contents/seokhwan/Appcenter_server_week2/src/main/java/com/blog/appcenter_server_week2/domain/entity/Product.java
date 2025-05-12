package com.blog.appcenter_server_week2.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product_table")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment
    private Long postId;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 20)
    private String location;

    @Column
    private Long heart;

    @Column(nullable = false)
    private Integer productState;

    // 작성자(유저)와의 다대일(N:1) 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id") // FK
    private User user;

    @Builder
    private Product(Long postId, Long price, String title, String description, String location, Long heart, Integer productState, User user) {
        this.postId = postId;
        this.price = price;
        this.title = title;
        this.description = description;
        this.location = location;
        this.heart = heart;
        this.productState = productState;
        this.user = user;
    }

    public Product update(Long price, String title, String description, String location, Long heart, Integer productState, User user) {
        this.price = price;
        this.title = title;
        this.description = description;
        this.location = location;
        this.heart = heart;
        this.productState = productState;
        this.user = user;
        return this;
    }
}
