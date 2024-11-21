package com.twofullmoon.howmuchmarket.dto;

import com.twofullmoon.howmuchmarket.entity.Product;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ProductPictureDTO {
    private String blobUrl;
    private Integer productId;
}
