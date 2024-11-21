package com.twofullmoon.howmuchmarket.repository;

import com.twofullmoon.howmuchmarket.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    public List<Transaction> findByBuyerId(String buyerId);
}
