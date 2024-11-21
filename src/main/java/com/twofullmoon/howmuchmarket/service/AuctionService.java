package com.twofullmoon.howmuchmarket.service;

import com.twofullmoon.howmuchmarket.dto.AuctionDTO;
import com.twofullmoon.howmuchmarket.entity.Auction;
import com.twofullmoon.howmuchmarket.entity.Bid;
import com.twofullmoon.howmuchmarket.entity.Product;
import com.twofullmoon.howmuchmarket.entity.User;
import com.twofullmoon.howmuchmarket.mapper.AuctionMapper;
import com.twofullmoon.howmuchmarket.mapper.BidMapper;
import com.twofullmoon.howmuchmarket.repository.AuctionRepository;
import com.twofullmoon.howmuchmarket.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class AuctionService {
    private final AuctionRepository auctionRepository;
    private final AuctionMapper auctionMapper;
    private final ProductRepository productRepository;
    private final BidMapper bidMapper;

    public AuctionService(AuctionRepository auctionRepository, AuctionMapper auctionMapper, ProductRepository productRepository, BidMapper bidMapper) {
        this.auctionRepository = auctionRepository;
        this.productRepository = productRepository;
        this.auctionMapper = auctionMapper;
        this.bidMapper = bidMapper;
    }

    public AuctionDTO getAuctionByProductId(int productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));

        Auction auction = auctionRepository.findByProduct(product).orElseThrow(() -> new IllegalArgumentException("Auction not found"));
        return auctionMapper.toDTO(
                auction,
                auction.getBids().stream()
                        .sorted((bid1, bid2) -> Integer.compare(bid2.getAmount(), bid1.getAmount()))
                        .map(bidMapper::toDTO)
                        .toList()
        );
    }

    public void createAuction(AuctionDTO auctionDTO) {
        Product product = productRepository.findById(auctionDTO.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));

        auctionRepository.findByProduct(product).ifPresent(auction -> {
            throw new IllegalArgumentException("Auction already exists for this product");
        });

        Auction auction = auctionMapper.toEntity(auctionDTO, product);

        auctionRepository.save(auction);
    }

    public void checkAndCloseAuctions() {
        LocalDateTime now = LocalDateTime.now();
        List<Auction> ongoingAuctions = auctionRepository.findByStatus("ongoing");

        for (Auction auction : ongoingAuctions) {
            if (auction.getEndTime().isBefore(now)) {
                auction.setStatus("ended");
                auctionRepository.save(auction);

                Product product = auction.getProduct();
                product.setProductStatus("auction_ended");
                productRepository.save(product);
            }
        }
    }

    public User getAuctionWinner(int auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid auction ID"));

        return auction.getBids().stream()
                .max(Comparator.comparing(Bid::getAmount))
                .map(Bid::getUser)
                .orElse(null);
    }
}
