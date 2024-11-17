package com.twofullmoon.howmuchmarket.mapper;

import com.twofullmoon.howmuchmarket.dto.BidDTO;
import com.twofullmoon.howmuchmarket.entity.Bid;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BidMapper {

    private final ProductMapper productMapper;

    BidMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public BidDTO toDTO(Bid bid) {
        return toDTO(bid, false);
    }

    public BidDTO toDTO(Bid bid, boolean includeProduct) {
        return BidDTO.builder()
                .amount(bid.getAmount())
                .userId(bid.getUser().getId())
                .auctionId(bid.getAuction().getId())
                .product(includeProduct ? Optional.of(productMapper.toDTO(bid.getAuction().getProduct())) : Optional.empty())
                .build();
    }
}
