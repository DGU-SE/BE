package com.twofullmoon.howmuchmarket.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "FraudReport")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FraudReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "description", nullable = false, length = 700)
    private String description;
}
