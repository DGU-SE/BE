package com.twofullmoon.howmuchmarket.dto;

import com.twofullmoon.howmuchmarket.entity.ProductPicture;
import com.twofullmoon.howmuchmarket.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ProductDTO {
    private Integer id;
    private String name;
    private Integer price;
    private LocalDateTime regTime;
    private LocalDateTime dealTime;
    private String productStatus;
    private String productDetail;
    private Boolean onAuction;
    private String userId;
    private long locationId;
    private List<ProductPictureDTO> productPictures;
}
