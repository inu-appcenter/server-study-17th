package com.example.domain.order;

import com.example.domain.product.Product;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;
}
