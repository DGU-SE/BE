package com.twofullmoon.howmuchmarket.mapper;

import com.twofullmoon.howmuchmarket.dto.ProductDTO;
import com.twofullmoon.howmuchmarket.dto.ProductPictureDTO;
import com.twofullmoon.howmuchmarket.entity.Product;
import com.twofullmoon.howmuchmarket.entity.ProductPicture;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ProductMapper {

    public ProductDTO toDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .regTime(product.getRegTime())
                .dealTime(product.getDealTime())
                .locationName(product.getLocationName())
                .productStatus(product.getProductStatus())
                .productDetail(product.getProductDetail())
                .onAuction(product.getOnAuction())
                .userId(product.getUser().getId())
                .longitude(product.getLongitude())
                .latitude(product.getLatitude()) 
                .build();
    }

    public ProductDTO toDTO(Product product, List<ProductPictureDTO> productPictures) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .regTime(product.getRegTime())
                .dealTime(product.getDealTime())
                .locationName(product.getLocationName())
                .productStatus(product.getProductStatus())
                .productDetail(product.getProductDetail())
                .onAuction(product.getOnAuction())
                .userId(product.getUser().getId())
                .longitude(product.getLongitude())
                .latitude(product.getLatitude())
                .productPictures(productPictures)
                .build();
    }
}
