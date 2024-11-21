package com.twofullmoon.howmuchmarket.mapper;

import com.twofullmoon.howmuchmarket.dto.ProductDTO;
import com.twofullmoon.howmuchmarket.entity.Product;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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
                .locationId(product.getLocation().getId())
                .build();
    }
}
