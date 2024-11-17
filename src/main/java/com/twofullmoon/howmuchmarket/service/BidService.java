package com.twofullmoon.howmuchmarket.service;

import com.twofullmoon.howmuchmarket.entity.Auction;
import com.twofullmoon.howmuchmarket.entity.Bid;
import com.twofullmoon.howmuchmarket.entity.User;
import com.twofullmoon.howmuchmarket.repository.AuctionRepository;
import com.twofullmoon.howmuchmarket.repository.BidRepository;
import com.twofullmoon.howmuchmarket.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BidService {
    private final BidRepository bidRepository;
    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;

    public BidService(BidRepository bidRepository, UserRepository userRepository, AuctionRepository auctionRepository) {
        this.bidRepository = bidRepository;
        this.userRepository = userRepository;
        this.auctionRepository = auctionRepository;
    }

    public List<Bid> getBidsByUser(String userId) {
        return bidRepository.findByUserId(userId);
    }

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
