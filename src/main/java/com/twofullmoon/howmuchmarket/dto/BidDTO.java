package com.twofullmoon.howmuchmarket.dto;

import com.twofullmoon.howmuchmarket.entity.Bid;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BidDTO {
    private Integer amount;
    private String userId;
    private Integer auctionId;
    private ProductDTO product = null;
    private String auctionWinner = null;
    private Integer auctionWinnerAmount = null;
}
