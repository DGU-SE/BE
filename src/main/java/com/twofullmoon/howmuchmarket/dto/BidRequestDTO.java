package com.twofullmoon.howmuchmarket.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BidRequestDTO {
    private Integer amount;
    private String userId;
    private Integer productId;
}
