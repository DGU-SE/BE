package com.twofullmoon.howmuchmarket.dto;

import com.twofullmoon.howmuchmarket.entity.Bid;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
@Builder
public class BidDTO {
    private Integer amount;
    private String userId;
    private Integer auctionId;
    private ProductDTO product = null;
    private String auctionWinner = null;
}
