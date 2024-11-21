package com.twofullmoon.howmuchmarket.mapper;

import com.twofullmoon.howmuchmarket.dto.LocationDTO;
import com.twofullmoon.howmuchmarket.dto.ProductDTO;
import com.twofullmoon.howmuchmarket.dto.ProductRequestDTO;
import com.twofullmoon.howmuchmarket.entity.Location;
import com.twofullmoon.howmuchmarket.entity.User;
import com.twofullmoon.howmuchmarket.dto.ProductPictureDTO;
import com.twofullmoon.howmuchmarket.entity.Product;
import com.twofullmoon.howmuchmarket.entity.ProductPicture;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ProductMapper {

    public ProductDTO toDTO(Product product) {
        return toDTO(product, null);
//        return ProductDTO.builder()
//        		.id(product.getId())
//        		.regTime(product.getRegTime())
//                .name(product.getName())
//                .price(product.getPrice())
//                .dealTime(product.getDealTime())
//                .productStatus(product.getProductStatus())
//                .productDetail(product.getProductDetail())
//                .onAuction(product.getOnAuction())
//                .userId(product.getUser().getId())
//                .locationId(product.getLocation().getId())
//                .build();
    }


    public Product toEntity(ProductRequestDTO dto, User user, Location location) {
        return Product.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .dealTime(dto.getDealTime())
                .productDetail(dto.getProductDetail())
                .onAuction(dto.getOnAuction())
                .user(user)
                .location(location)
                .build();
    }


    public ProductDTO toDTO(Product product, List<ProductPictureDTO> productPictures) {
        return toDTO(product, productPictures, null);
    }

    public ProductDTO toDTO(Product product, List<ProductPictureDTO> productPictures, Double distanceKiloMeter) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .regTime(product.getRegTime())
                .dealTime(product.getDealTime())
                .productStatus(product.getProductStatus())
                .productDetail(product.getProductDetail())
                .onAuction(product.getOnAuction())
                .userId(product.getUser().getId())
                .locationId(product.getLocation().getId())
                .productPictures(productPictures)
                .distanceKiloMeter(distanceKiloMeter)
                .build();
    }
}
