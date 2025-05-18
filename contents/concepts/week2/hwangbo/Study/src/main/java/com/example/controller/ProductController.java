package com.example.controller;

import com.example.api.ProductApiSpecification;
import com.example.dto.product.ProductQuantityUpdateRequest;
import com.example.dto.product.ProductRequest;
import com.example.dto.product.ProductResponse;
import com.example.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController implements ProductApiSpecification {

    private final ProductService productService;

    @Override
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid ProductRequest request) {
        ProductResponse dto = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/{id}/quantity")
    public ResponseEntity<ProductResponse> updateQuantity(
            @PathVariable Long id,
            @RequestBody @Valid ProductQuantityUpdateRequest request) {
        ProductResponse dto = productService.updateQuantity(id, request.getQuantity());
        return ResponseEntity.ok(dto);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<ProductResponse>> listProducts() {
        List<ProductResponse> dtos = productService.findAllProducts();
        return ResponseEntity.ok(dtos);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        ProductResponse dto = productService.findProductById(id);
        return ResponseEntity.ok(dto);
    }
}
