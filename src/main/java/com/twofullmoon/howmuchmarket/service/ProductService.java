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

    // 특정 사용자의 상품 목록 조회
    public List<Product> getProductsByUserId(String userId) {
        return productRepository.findByUserId(userId);
    }
}


