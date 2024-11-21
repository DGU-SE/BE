package com.twofullmoon.howmuchmarket.controller;

import com.twofullmoon.howmuchmarket.dto.TransactionDTO;
import com.twofullmoon.howmuchmarket.dto.TransactionRequestDTO;
import com.twofullmoon.howmuchmarket.service.TransactionService;
import com.twofullmoon.howmuchmarket.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    private final TransactionService transactionService;
    private final JwtUtil jwtUtil;

    public TransactionController(TransactionService transactionService, JwtUtil jwtUtil) {
        this.transactionService = transactionService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/purchase")
    public TransactionDTO purchaseProduct(@RequestBody TransactionRequestDTO transactionRequestDTO, @RequestHeader("Authorization") String authorizationHeader) {
        String tokenUserId = jwtUtil.extractUserId(authorizationHeader.substring(7));

        return transactionService.saveNewPurchase(transactionRequestDTO.getProductId(), tokenUserId);
    }

    @GetMapping("/purchased")
    public ResponseEntity<List<TransactionDTO>> getPurchasedProducts(@RequestHeader("Authorization") String authorizationHeader) {
        String tokenUserId = jwtUtil.extractUserId(authorizationHeader.substring(7));
        List<TransactionDTO> transactions = transactionService.getTransactionsByUserId(tokenUserId, true);

        return ResponseEntity.ok(transactions);
    }


}
