package com.example.api;

import com.example.dto.product.ProductQuantityUpdateRequest;
import com.example.dto.product.ProductRequest;
import com.example.dto.product.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import java.util.List;

@Tag(name = "Product", description = "상품 관련 API")
public interface ProductApiSpecification {

    @Operation(summary = "상품 생성", description = "새 상품을 생성하고 생성된 상품 정보를 반환")
    ResponseEntity<ProductResponse> createProduct(
            @RequestBody @Valid ProductRequest req
    );

    @Operation(summary = "상품 삭제", description = "ID로 상품을 삭제")
    ResponseEntity<Void> deleteProduct(
            @PathVariable("id") Long id
    );

    @Operation(summary = "상품 수량 수정", description = "상품 ID와 변경할 수량으로 상품 수량을 수정")
    ResponseEntity<ProductResponse> updateQuantity(
            @PathVariable("id") Long id,
            @RequestBody @Valid ProductQuantityUpdateRequest req
    );

    @Operation(summary = "전체 상품 조회", description = "모든 상품 목록을 조회")
    ResponseEntity<List<ProductResponse>> listProducts();

    @Operation(summary = "상품 상세 조회", description = "ID로 특정 상품의 상세 정보를 조회")
    ResponseEntity<ProductResponse> getProduct(
            @PathVariable("id") Long id
    );
}
