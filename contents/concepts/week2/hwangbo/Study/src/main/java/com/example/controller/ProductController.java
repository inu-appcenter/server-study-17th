package com.example.controller;

import com.example.DTO.product.ProductQuantityUpdateRequest;
import com.example.DTO.product.ProductRequest;
import com.example.DTO.product.ProductResponse;
import com.example.domain.product.Product;
import com.example.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Product", description = "상품 관련 API")
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "상품 생성", description = "새 상품을 생성하고 생성된 상품 정보를 반환")
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Parameter(description = "생성할 상품 정보", required = true)
            @RequestBody @Valid ProductRequest req) {
        Product saved = productService.createProduct(req);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ProductResponse.of(saved));
    }

    @Operation(summary = "상품 삭제", description = "ID로 상품을 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "삭제할 상품 ID", required = true, example = "1")
            @PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "상품 수량 수정", description = "상품 ID와 변경할 수량으로 상품 수량을 수정")
    @PatchMapping("/{id}/quantity")
    public ResponseEntity<ProductResponse> updateQuantity(
            @Parameter(description = "수량을 변경할 상품 ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "변경할 수량 정보", required = true)
            @RequestBody @Valid ProductQuantityUpdateRequest req) {
        Product updated = productService.updateQuantity(id, req.getQuantity());
        return ResponseEntity.ok(ProductResponse.of(updated));
    }

    @Operation(summary = "전체 상품 조회", description = "모든 상품 목록을 조회")
    @GetMapping
    public ResponseEntity<List<ProductResponse>> listProducts() {
        List<Product> all = productService.findAll();
        List<ProductResponse> dtos = all.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "상품 상세 조회", description = "ID로 특정 상품의 상세 정보를 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(
            @Parameter(description = "조회할 상품 ID", required = true, example = "1")
            @PathVariable Long id) {
        Product product = productService.findById(id);
        return ResponseEntity.ok(ProductResponse.of(product));
    }
}
