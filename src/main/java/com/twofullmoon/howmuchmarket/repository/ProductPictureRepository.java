package com.twofullmoon.howmuchmarket.repository;

import com.twofullmoon.howmuchmarket.entity.ProductPicture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductPictureRepository extends JpaRepository<ProductPicture, Long> {
    public List<ProductPicture> findByProductId(int productId);
    public void deleteAllByProductId(int productId);
}
