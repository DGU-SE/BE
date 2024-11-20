package com.twofullmoon.howmuchmarket.controller;

import com.twofullmoon.howmuchmarket.entity.Product;
import com.twofullmoon.howmuchmarket.service.ProductService;
import com.twofullmoon.howmuchmarket.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;
    private final JwtUtil jwtUtil;

    public ProductController(ProductService productService, JwtUtil jwtUtil) {
        this.productService = productService;
        this.jwtUtil = jwtUtil;
    }

    // 올린 상품 목록 조회
    @GetMapping("/my")
    public ResponseEntity<List<Product>> getMyProducts(@RequestHeader("Authorization") String token) {
        // JWT에서 userId 추출
        String userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        List<Product> products = productService.getProductsByUserId(userId);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam String keyword,
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam Integer lowBound,
            @RequestParam Integer upBound,
            @RequestParam String productStatus
    ) {
        List<Product> products = productService.searchProducts(keyword, latitude, longitude, lowBound, upBound, productStatus);
        return ResponseEntity.ok(products);
    }
}
