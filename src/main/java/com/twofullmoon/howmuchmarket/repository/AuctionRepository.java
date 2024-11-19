package com.twofullmoon.howmuchmarket.repository;

import com.twofullmoon.howmuchmarket.entity.Auction;
import com.twofullmoon.howmuchmarket.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuctionRepository extends JpaRepository<Auction, Integer> {
    public Optional<Auction> findByProduct(Product product);
    public List<Auction> findByStatus(String status);
}
