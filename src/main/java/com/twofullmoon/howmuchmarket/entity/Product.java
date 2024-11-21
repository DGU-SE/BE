package com.twofullmoon.howmuchmarket.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "reg_time", nullable = false)
    private LocalDateTime regTime;

    @Column(name = "deal_time", nullable = false)
    private LocalDateTime dealTime;

    @Column(name = "location_name", nullable = false, length = 100)
    private String locationName;

    @Column(name = "product_status", nullable = false, length = 45)
    private String productStatus = "unsold"; // "unsold", "sold", "auction_ended"

    @Column(name = "product_detail", nullable = false, length = 700)
    private String productDetail;

    @Column(name = "on_auction", nullable = false)
    private Boolean onAuction;
    
    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @ManyToOne
    @JoinColumn(name = "User_id", referencedColumnName = "id", nullable = false)
    private User user;

    @OneToMany(mappedBy="product", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<ProductPicture> productPictures;

    @OneToMany(mappedBy="product", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;

    @OneToOne(mappedBy="product", cascade=CascadeType.ALL, orphanRemoval = true)
    private Auction auction;
}