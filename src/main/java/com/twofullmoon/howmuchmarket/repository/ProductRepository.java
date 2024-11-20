package com.twofullmoon.howmuchmarket.repository;

import com.twofullmoon.howmuchmarket.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	// 사용자 ID로 상품 조회
    List<Product> findByUserId(String userId);

    // 아래의 Query 필요한가?
    @Query("""
        SELECT p FROM Product p 
        JOIN p.user u 
        JOIN Location l ON l.id = u.location.id
        WHERE p.name LIKE %:keyword% 
        AND p.price BETWEEN :lowBound AND :upBound 
        AND p.productStatus = :productStatus
        AND (6371 * acos(cos(radians(:latitude)) 
        * cos(radians(l.latitude)) 
        * cos(radians(l.longitude) - radians(:longitude)) 
        + sin(radians(:latitude)) 
        * sin(radians(l.latitude)))) <= 30
        ORDER BY p.regTime DESC
    """)
    List<Product> searchProducts(
        @Param("keyword") String keyword,
        @Param("latitude") Double latitude,
        @Param("longitude") Double longitude,
        @Param("lowBound") Integer lowBound,
        @Param("upBound") Integer upBound,
        @Param("productStatus") String productStatus
    );
}