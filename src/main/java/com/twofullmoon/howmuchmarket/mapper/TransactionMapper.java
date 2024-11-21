package com.twofullmoon.howmuchmarket.mapper;

import com.twofullmoon.howmuchmarket.dto.TransactionDTO;
import com.twofullmoon.howmuchmarket.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public TransactionDTO toDTO(Transaction transaction) {
        return TransactionDTO.builder()
                .finalPrice(transaction.getFinalPrice())
                .status(transaction.getStatus())
                .productId(transaction.getProduct().getId())
                .buyerId(transaction.getBuyer().getId())
                .sellerId(transaction.getSeller().getId())
                .build();
    }
}
