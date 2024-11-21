package com.twofullmoon.howmuchmarket.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchCriteriaDTO {
    private String keyword;
    private Double latitude;
    private Double longitude;
    private Integer lowBound;
    private Integer upBound;
    private String productStatus;
}
