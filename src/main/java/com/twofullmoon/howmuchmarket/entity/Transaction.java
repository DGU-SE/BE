package com.twofullmoon.howmuchmarket.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "final_price", nullable = false)
    private Integer finalPrice;

    @Column(name = "status", nullable = false, length = 45, columnDefinition = "VARCHAR(45) DEFAULT 'processing'")
    private String status = "processing"; // "processing", "completed"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

}
