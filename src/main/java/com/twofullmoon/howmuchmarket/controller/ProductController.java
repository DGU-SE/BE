package com.twofullmoon.howmuchmarket.controller;

import com.twofullmoon.howmuchmarket.dto.ProductDTO;
import com.twofullmoon.howmuchmarket.dto.ProductRequestDTO; // ProductRequest 추가
import com.twofullmoon.howmuchmarket.entity.Location;
import com.twofullmoon.howmuchmarket.entity.Product;
import com.twofullmoon.howmuchmarket.entity.User;
import com.twofullmoon.howmuchmarket.mapper.ProductMapper;
import com.twofullmoon.howmuchmarket.service.LocationService; // LocationService 추가
import com.twofullmoon.howmuchmarket.service.ProductService;
import com.twofullmoon.howmuchmarket.service.UserService;
import com.twofullmoon.howmuchmarket.util.JwtUtil;

import org.springframework.http.HttpStatus;
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
    private final UserService userService;
    private final ProductMapper productMapper;

    public ProductController(ProductService productService, LocationService locationService, JwtUtil jwtUtil, UserService userService, ProductMapper productMapper) {
        this.productService = productService;
        this.locationService = locationService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.productMapper = productMapper;
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
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestHeader("Authorization") String token,
            @RequestBody ProductRequestDTO request) 
    {
    	String userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
    	
    	if (!userId.equals(request.getUserId())) {
    		throw new IllegalArgumentException("userId in token and request body are not same");
    	}
    	
    	Product product = productService.createProduct(request);
    	ProductDTO productDTO = productMapper.toDTO(product);
    	
    	return ResponseEntity.status(HttpStatus.CREATED).body(productDTO);
    }

}
