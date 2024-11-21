package com.twofullmoon.howmuchmarket.controller;

import com.twofullmoon.howmuchmarket.dto.ProductRequestDTO; // ProductRequest 추가
import com.twofullmoon.howmuchmarket.entity.Location;
import com.twofullmoon.howmuchmarket.entity.Product;
import com.twofullmoon.howmuchmarket.service.LocationService; // LocationService 추가
import com.twofullmoon.howmuchmarket.service.ProductService;
import com.twofullmoon.howmuchmarket.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;
    private final LocationService locationService; // LocationService 의존성 추가
    private final JwtUtil jwtUtil;

    public ProductController(ProductService productService, LocationService locationService, JwtUtil jwtUtil) {
        this.productService = productService;
        this.locationService = locationService;
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
    
    // 상품 검색
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
    
    // 상품 등록
    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestHeader("Authorization") String token,
            @RequestBody ProductRequestDTO request) 
    {

    	User user = userService.findUserById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
    	
        // Location ID로 Location 조회
        Location location = locationService.findLocationById(request.getLocationId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid location ID"));
        
        // Product 생성
        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .dealTime(request.getDealTime())
                .location(location) // Location 설정
                .productDetail(request.getProductDetail())
                .onAuction(request.getOnAuction())
                .productStatus("unsold")
                .regTime(LocalDateTime.now()) // 자동 설정
                .build();
        
        // Product 저장
        productService.save(product);
        return ResponseEntity.ok(product);
    }

    // 추가적으로 필요한 API는 요청에 따라 구현 가능
}
