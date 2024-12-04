package com.twofullmoon.howmuchmarket.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FraudReportDTO {
    private Integer productId;
    private String description;
}
