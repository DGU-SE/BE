package com.twofullmoon.howmuchmarket.dto;

import com.twofullmoon.howmuchmarket.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ProductDTO {
    private Integer id;
    private String name;
    private Integer price;
    private LocalDateTime regTime;
    private LocalDateTime dealTime;
    private String locationName;
    private String productStatus;
    private String productDetail;
    private Boolean onAuction;
    private String userId;
    private Double longitude;
    private Double latitude; 
}
