package com.twofullmoon.howmuchmarket.mapper;

import com.twofullmoon.howmuchmarket.dto.AuctionDTO;
import com.twofullmoon.howmuchmarket.dto.BidDTO;
import com.twofullmoon.howmuchmarket.entity.Auction;
import com.twofullmoon.howmuchmarket.entity.Bid;
import com.twofullmoon.howmuchmarket.entity.Product;
import org.springframework.stereotype.Component;

import java.util.List;

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
        auctionDTO.setCurrentPrice(auction.getCurrentPrice());
        auctionDTO.setStartTime(auction.getStartTime());
        auctionDTO.setEndTime(auction.getEndTime());
        return auctionDTO;
    }

    public AuctionDTO toDTO(Auction auction, List<BidDTO> bids) {
        AuctionDTO auctionDTO = toDTO(auction);
        auctionDTO.setBids(bids);
        return auctionDTO;
    }
}