package com.twofullmoon.howmuchmarket.mapper;

import com.twofullmoon.howmuchmarket.dto.AuctionDTO;
import com.twofullmoon.howmuchmarket.entity.Auction;
import com.twofullmoon.howmuchmarket.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class AuctionMapper {

    public Auction toEntity(AuctionDTO auctionDTO, Product product) {
        return Auction.builder()
                .product(product)
                .startPrice(auctionDTO.getStartPrice())
                .currentPrice(auctionDTO.getStartPrice())
                .status("ongoing")
                .startTime(auctionDTO.getStartTime())
                .endTime(auctionDTO.getEndTime())
                .build();
    }

    public AuctionDTO toDTO(Auction auction) {
        AuctionDTO auctionDTO = new AuctionDTO();
        auctionDTO.setProductId(auction.getProduct().getId());
        auctionDTO.setStartPrice(auction.getStartPrice());
        auctionDTO.setStartTime(auction.getStartTime());
        auctionDTO.setEndTime(auction.getEndTime());
        return auctionDTO;
    }
}