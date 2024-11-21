package com.twofullmoon.howmuchmarket.service;

import com.twofullmoon.howmuchmarket.dto.LocationDTO;
import com.twofullmoon.howmuchmarket.dto.ProductRequestDTO;
import com.twofullmoon.howmuchmarket.dto.UserResponseDTO;
import com.twofullmoon.howmuchmarket.entity.Location;
import com.twofullmoon.howmuchmarket.entity.Product;
import com.twofullmoon.howmuchmarket.entity.User;
import com.twofullmoon.howmuchmarket.mapper.LocationMapper;
import com.twofullmoon.howmuchmarket.mapper.ProductMapper;
import com.twofullmoon.howmuchmarket.mapper.UserMapper;
import com.twofullmoon.howmuchmarket.repository.LocationRepository;
import com.twofullmoon.howmuchmarket.repository.ProductRepository;
import com.twofullmoon.howmuchmarket.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;
    private final UserRepository userRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, LocationRepository locationRepository, LocationMapper locationMapper, UserRepository userRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
        this.userRepository = userRepository;
        this.productMapper = productMapper;
    }
    
//    public Product createProduct(Product product) {
//        return productRepository.save(product);
//    }
    //상품 등록
    public Product createProduct(ProductRequestDTO productRequestDTO) {
    	LocationDTO locationDTO = productRequestDTO.getLocationDTO();
    	Location location = locationMapper.toEntity(locationDTO);
    	
    	locationRepository.save(location);
    	
    	User seller = userRepository.findById(productRequestDTO.getUserId()).orElseThrow(() -> new IllegalArgumentException("User not found"));
    	
    	
//    	UserResponseDTO userResponseDTO = productRequestDTO.getUserResponseDTO();
//    	User user = UserMapper.toEntity(userResponseDTO);
    			
    			
//    	ProductRequestDTO productDTO = productRequestDTO.getProductRequestDTO();
    	Product product = productMapper.toEntity(productRequestDTO, seller, location);
    	
    	
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
    
    public Product save(Product product) {
        return productRepository.save(product);
    }
    
}


