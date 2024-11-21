package com.twofullmoon.howmuchmarket.service;

import com.twofullmoon.howmuchmarket.dto.BidDTO;
import com.twofullmoon.howmuchmarket.dto.ProductDTO;
import com.twofullmoon.howmuchmarket.dto.ProductPictureDTO;
import com.twofullmoon.howmuchmarket.entity.Auction;
import com.twofullmoon.howmuchmarket.entity.Bid;
import com.twofullmoon.howmuchmarket.entity.User;
import com.twofullmoon.howmuchmarket.mapper.BidMapper;
import com.twofullmoon.howmuchmarket.mapper.ProductMapper;
import com.twofullmoon.howmuchmarket.mapper.ProductPictureMapper;
import com.twofullmoon.howmuchmarket.repository.AuctionRepository;
import com.twofullmoon.howmuchmarket.repository.BidRepository;
import com.twofullmoon.howmuchmarket.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BidService {
    private final BidRepository bidRepository;
    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;
    private final AuctionService auctionService;
    private final BidMapper bidMapper;
    private final ProductMapper productMapper;
    private final ProductPictureMapper productPictureMapper;

    public BidService(BidRepository bidRepository, UserRepository userRepository, AuctionRepository auctionRepository, AuctionService auctionService, BidMapper bidMapper, ProductMapper productMapper, ProductPictureMapper productPictureMapper) {
        this.bidRepository = bidRepository;
        this.userRepository = userRepository;
        this.auctionRepository = auctionRepository;
        this.auctionService = auctionService;
        this.bidMapper = bidMapper;
        this.productMapper = productMapper;
        this.productPictureMapper = productPictureMapper;
    }

    public List<Bid> getBidsByUser(String userId) {
        return bidRepository.findByUserId(userId);
    }

    public BidDTO getBidDTO(Bid bid, boolean includeProduct, boolean includeAuctionWinner) {
        return getBidDTO(bid, includeProduct, includeAuctionWinner, false);
    }

    public BidDTO getBidDTO(Bid bid, boolean includeProduct, boolean includeAuctionWinner, boolean includeAuctionWinnerAmount) {
        String auctionWinnerId = includeAuctionWinner ? auctionService.getAuctionWinner(bid.getAuction().getId()).getId() : null;
        List<ProductPictureDTO> productPictureDTOs = bid.getAuction().getProduct().getProductPictures().stream()
                .map(productPictureMapper::toDTO)
                .toList();
        ProductDTO productDTO = includeProduct ? productMapper.toDTO(bid.getAuction().getProduct(), productPictureDTOs) : null;
        Integer auctionWinnerAmount = includeAuctionWinnerAmount ? bid.getAuction().getCurrentPrice() : null;
        return bidMapper.toDTO(bid, Optional.ofNullable(productDTO), Optional.ofNullable(auctionWinnerId), Optional.ofNullable(auctionWinnerAmount));
    }

    public List<BidDTO> getMergedBidsByHighestAmount(String userId) {
        return getBidsByUser(userId).stream()
                .map(bid -> getBidDTO(bid, true, true, true))
                .collect(Collectors.groupingBy(
                        bid -> bid.getProduct().getId(),
                        Collectors.maxBy(Comparator.comparingInt(BidDTO::getAmount))
                ))
                .values()
                .stream()
                .flatMap(Optional::stream)
                .toList();
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
