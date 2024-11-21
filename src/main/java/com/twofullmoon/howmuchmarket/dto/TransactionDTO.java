package com.twofullmoon.howmuchmarket.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TransactionDTO {
    private Integer finalPrice;
    private String status;
    private Integer productId;
    private String buyerId;
    private String sellerId;
    private ProductDTO product;
}
