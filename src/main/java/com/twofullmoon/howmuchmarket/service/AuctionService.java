package com.twofullmoon.howmuchmarket.service;

import com.twofullmoon.howmuchmarket.dto.AuctionDTO;
import com.twofullmoon.howmuchmarket.entity.Auction;
import com.twofullmoon.howmuchmarket.entity.Bid;
import com.twofullmoon.howmuchmarket.entity.Product;
import com.twofullmoon.howmuchmarket.entity.User;
import com.twofullmoon.howmuchmarket.mapper.AuctionMapper;
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

    public AuctionService(AuctionRepository auctionRepository, AuctionMapper auctionMapper, ProductRepository productRepository) {
        this.auctionRepository = auctionRepository;
        this.productRepository = productRepository;
        this.auctionMapper = auctionMapper;
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
