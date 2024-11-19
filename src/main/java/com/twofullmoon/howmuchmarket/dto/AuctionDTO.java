package com.twofullmoon.howmuchmarket.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AuctionDTO {
    private Integer productId;
    private Integer startPrice;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
