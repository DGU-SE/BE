package com.twofullmoon.howmuchmarket.repository;

import com.twofullmoon.howmuchmarket.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Integer> {
    public List<Bid> findByUserId(String userId);
}
