package com.twofullmoon.howmuchmarket.service;

import com.twofullmoon.howmuchmarket.dto.BidDTO;
import com.twofullmoon.howmuchmarket.dto.ProductDTO;
import com.twofullmoon.howmuchmarket.dto.ProductPictureDTO;
import com.twofullmoon.howmuchmarket.entity.Auction;
import com.twofullmoon.howmuchmarket.entity.Bid;
import com.twofullmoon.howmuchmarket.entity.Product;
import com.twofullmoon.howmuchmarket.entity.User;
import com.twofullmoon.howmuchmarket.mapper.BidMapper;
import com.twofullmoon.howmuchmarket.mapper.ProductMapper;
import com.twofullmoon.howmuchmarket.mapper.ProductPictureMapper;
import com.twofullmoon.howmuchmarket.repository.AuctionRepository;
import com.twofullmoon.howmuchmarket.repository.BidRepository;
import com.twofullmoon.howmuchmarket.repository.ProductRepository;
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
    private final ProductRepository productRepository;

    public BidService(BidRepository bidRepository, UserRepository userRepository, AuctionRepository auctionRepository, AuctionService auctionService, BidMapper bidMapper, ProductMapper productMapper, ProductPictureMapper productPictureMapper, ProductRepository productRepository) {
        this.bidRepository = bidRepository;
        this.userRepository = userRepository;
        this.auctionRepository = auctionRepository;
        this.auctionService = auctionService;
        this.bidMapper = bidMapper;
        this.productMapper = productMapper;
        this.productPictureMapper = productPictureMapper;
        this.productRepository = productRepository;
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
    public BidDTO createBid(String userId, int productId, int amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));
        if (!product.getOnAuction()) {
            throw new IllegalArgumentException("Product is not on auction");
        }
        if (product.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("User cannot bid on their own product");
        }

        Auction auction = product.getAuction();

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

        return bidMapper.toDTO(bidRepository.save(bid));
    }
}
