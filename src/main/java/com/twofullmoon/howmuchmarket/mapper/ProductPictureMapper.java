package com.twofullmoon.howmuchmarket.mapper;

import com.twofullmoon.howmuchmarket.dto.ProductPictureDTO;
import com.twofullmoon.howmuchmarket.entity.ProductPicture;
import org.springframework.stereotype.Component;

@Component
public class ProductPictureMapper {
    public ProductPictureDTO toDTO(ProductPicture productPicture) {
        return ProductPictureDTO.builder()
                .productId(productPicture.getProduct().getId())
                .blobUrl(productPicture.getBlobUrl())
                .build();
    }
}
