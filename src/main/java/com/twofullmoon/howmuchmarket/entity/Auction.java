package com.twofullmoon.howmuchmarket.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Auction")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "start_price", nullable = false)
    private Integer startPrice;

    @Column(name = "current_price", nullable = false)
    private Integer currentPrice = 0;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "ongoing"; // "ongoing", "ended"

    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private Product product;

    @OneToMany(mappedBy = "auction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bid> bids = new ArrayList<>();
}
