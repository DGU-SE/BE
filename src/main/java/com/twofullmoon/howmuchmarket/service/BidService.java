package com.twofullmoon.howmuchmarket.service;

import com.twofullmoon.howmuchmarket.dto.BidDTO;
import com.twofullmoon.howmuchmarket.dto.ProductDTO;
import com.twofullmoon.howmuchmarket.entity.Auction;
import com.twofullmoon.howmuchmarket.entity.Bid;
import com.twofullmoon.howmuchmarket.entity.User;
import com.twofullmoon.howmuchmarket.mapper.BidMapper;
import com.twofullmoon.howmuchmarket.mapper.ProductMapper;
import com.twofullmoon.howmuchmarket.repository.AuctionRepository;
import com.twofullmoon.howmuchmarket.repository.BidRepository;
import com.twofullmoon.howmuchmarket.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BidService {
    private final BidRepository bidRepository;
    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;
    private final AuctionService auctionService;
    private final BidMapper bidMapper;
    private final ProductMapper productMapper;

    public BidService(BidRepository bidRepository, UserRepository userRepository, AuctionRepository auctionRepository, AuctionService auctionService, BidMapper bidMapper, ProductMapper productMapper) {
        this.bidRepository = bidRepository;
        this.userRepository = userRepository;
        this.auctionRepository = auctionRepository;
        this.auctionService = auctionService;
        this.bidMapper = bidMapper;
        this.productMapper = productMapper;
    }

    public List<Bid> getBidsByUser(String userId) {
        return bidRepository.findByUserId(userId);
    }

    public BidDTO getBidDTO(Bid bid, boolean includeProduct, boolean includeAuctionWinner) {
        String auctionWinnerId = includeAuctionWinner ? auctionService.getAuctionWinner(bid.getAuction().getId()).getId() : null;
        ProductDTO productDTO = includeProduct ? productMapper.toDTO(bid.getAuction().getProduct()) : null;
        return bidMapper.toDTO(bid, Optional.ofNullable(productDTO), Optional.ofNullable(auctionWinnerId));
    }

    @Transactional
    public void createBid(String userId, int auctionId, int amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid auction ID"));

        if (!auction.getStatus().equals("ongoing")) {
            throw new IllegalArgumentException("Auction is not ongoing");
        }
        if (auction.getCurrentPrice() >= amount) {
            throw new IllegalArgumentException("Bid amount must be greater than the current price");
        }

        auction.setCurrentPrice(amount);
        auctionRepository.save(auction);

        Bid bid = Bid.builder()
                .user(user)
                .auction(auction)
                .amount(amount)
                .build();

        bidRepository.save(bid);
    }
}
