package com.twofullmoon.howmuchmarket.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

<<<<<<< HEAD
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
=======
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
>>>>>>> dev
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
<<<<<<< HEAD
    private String productStatus;
=======
    private String productStatus = "unsold";
>>>>>>> dev

    @Column(name = "product_detail", nullable = false, length = 700)
    private String productDetail;

    @Column(name = "on_auction", nullable = false)
    private Boolean onAuction;

    @ManyToOne
    @JoinColumn(name = "User_id", referencedColumnName = "id", nullable = false)
    private User user;
<<<<<<< HEAD
}
=======
}
>>>>>>> dev
