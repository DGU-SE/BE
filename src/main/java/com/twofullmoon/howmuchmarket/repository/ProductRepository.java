package com.twofullmoon.howmuchmarket.repository;

import com.twofullmoon.howmuchmarket.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByUserId(String userId);
}
