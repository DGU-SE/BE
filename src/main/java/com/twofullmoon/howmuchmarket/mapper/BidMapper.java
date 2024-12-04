package com.twofullmoon.howmuchmarket.mapper;

import com.twofullmoon.howmuchmarket.dto.BidDTO;
import com.twofullmoon.howmuchmarket.dto.ProductDTO;
import com.twofullmoon.howmuchmarket.entity.Bid;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BidMapper {
    public BidDTO toDTO(Bid bid) {
        return toDTO(bid, Optional.empty(), Optional.empty());
    }

    public BidDTO toDTO(Bid bid, Optional<ProductDTO> productDTO, Optional<String> auctionWinnerId) {
        return toDTO(bid, productDTO, auctionWinnerId, Optional.empty());
    }

    public BidDTO toDTO(Bid bid, Optional<ProductDTO> productDTO, Optional<String> auctionWinnerId, Optional<Integer> auctionWinnerAmount) {
        return BidDTO.builder()
                .amount(bid.getAmount())
                .userId(bid.getUser().getId())
                .auctionId(bid.getAuction().getId())
                .product(productDTO.orElse(null))
                .auctionWinner(auctionWinnerId.orElse(null))
                .auctionWinnerAmount(auctionWinnerAmount.orElse(null))
                .build();
    }
}
