package com.twofullmoon.howmuchmarket.service;

import com.twofullmoon.howmuchmarket.dto.TransactionDTO;
import com.twofullmoon.howmuchmarket.entity.Product;
import com.twofullmoon.howmuchmarket.entity.Transaction;
import com.twofullmoon.howmuchmarket.entity.User;
import com.twofullmoon.howmuchmarket.mapper.ProductMapper;
import com.twofullmoon.howmuchmarket.mapper.TransactionMapper;
import com.twofullmoon.howmuchmarket.repository.ProductRepository;
import com.twofullmoon.howmuchmarket.repository.TransactionRepository;
import com.twofullmoon.howmuchmarket.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final AuctionService auctionService;
    private final TransactionMapper transactionMapper;
    private final ProductMapper productMapper;

    public TransactionService(TransactionRepository transactionRepository, ProductRepository productRepository, UserRepository userRepository, AuctionService auctionService, TransactionMapper transactionMapper, ProductMapper productMapper) {
        this.transactionRepository = transactionRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.auctionService = auctionService;
        this.transactionMapper = transactionMapper;
        this.productMapper = productMapper;
    }

    public TransactionDTO getTransactionDTO(Transaction transaction, boolean includeProduct) {
        TransactionDTO transactionDTO = transactionMapper.toDTO(transaction);
        if (includeProduct) {
            transactionDTO.setProduct(productMapper.toDTO(transaction.getProduct()));
        }
        return transactionDTO;
    }

    public List<TransactionDTO> getTransactionsByUserId(String userId, boolean includeProduct) {
        List<Transaction> transactions = transactionRepository.findByBuyerId(userId);
        return transactions.stream()
                .map(transaction -> getTransactionDTO(transaction, includeProduct))
                .collect(Collectors.toList());
    }

    @Transactional
    public TransactionDTO saveNewPurchase(int productId, String buyerId) {
        int finalPrice;
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));
        User buyer = userRepository.findById(buyerId).orElseThrow(() -> new IllegalArgumentException("Invalid buyer ID"));
        User seller = product.getUser();

        if (buyer.getId().equals(seller.getId())) {
            throw new IllegalArgumentException("Buyer and seller cannot be the same user");
        }
        if (product.getProductStatus().equals("sold")) {
            throw new IllegalArgumentException("Product has already been sold");
        }

        if (product.getOnAuction()) {
            if (!product.getProductStatus().equals("auction_ended")) {
                throw new IllegalArgumentException("Auction has not ended yet");
            }

            if (!auctionService.getAuctionWinner(product.getAuction().getId()).getId().equals(buyerId)) {
                throw new IllegalArgumentException("Buyer is not the winner of the auction");
            }

            finalPrice = product.getAuction().getCurrentPrice();
        } else {
            finalPrice = product.getPrice();
        }

        Transaction transaction = Transaction.builder()
                .finalPrice(finalPrice)
                .status("completed")
                .product(product)
                .buyer(buyer)
                .seller(seller)
                .build();

        transactionRepository.save(transaction);

        product.setProductStatus("sold");
        productRepository.save(product);

        return transactionMapper.toDTO(transaction);
    }
}
