package com.blog.appcenter_server_week2.controller;

import com.blog.appcenter_server_week2.dto.product.ProductUploadRequestDto;
import com.blog.appcenter_server_week2.dto.product.ProductUploadResponseDto;
import com.blog.appcenter_server_week2.dto.product.ProductListResponseDto;
import com.blog.appcenter_server_week2.jwt.UserDetailsImpl;
import com.blog.appcenter_server_week2.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductUploadResponseDto> addProduct(@RequestParam Long userId, @Valid @RequestBody ProductUploadRequestDto productUploadRequestDto) {
        ProductUploadResponseDto product = productService.addProduct(userId, productUploadRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @GetMapping
    public ResponseEntity<List<ProductListResponseDto>> getProducts() {
        List<ProductListResponseDto> products = productService.getProducts();
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<ProductUploadResponseDto> updateProduct(@PathVariable Long postId, @Valid @RequestBody ProductUploadRequestDto productUploadRequestDto) {
        ProductUploadResponseDto product = productService.updateProduct(postId, productUploadRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ProductUploadResponseDto> deleteProduct(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        productService.deleteProduct(postId, userDetails.getUser().getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
