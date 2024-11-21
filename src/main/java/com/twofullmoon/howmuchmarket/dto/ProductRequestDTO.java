package com.twofullmoon.howmuchmarket.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProductRequestDTO {
    private String name;
    private Integer price;
    private LocalDateTime dealTime;
    private Integer locationId; // Location ID를 사용
    private String productDetail;
    private Boolean onAuction;
}
