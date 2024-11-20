package com.twofullmoon.howmuchmarket.service;

import com.twofullmoon.howmuchmarket.entity.Product;
import com.twofullmoon.howmuchmarket.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    // 특정 사용자의 상품 목록 조회
    public List<Product> getProductsByUserId(String userId) {
        return productRepository.findByUserId(userId);
    }
    
    // 상품 검색 기능	
    public List<Product> searchProducts(String keyword, Double latitude, Double longitude, Integer lowBound, Integer upBound, String productStatus) {
    	return productRepository.searchProducts(keyword, latitude, longitude, lowBound, upBound, productStatus);
    }
    
    
}


