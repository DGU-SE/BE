package com.twofullmoon.howmuchmarket.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ProductPicture")
public class ProductPicture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "blob_url", nullable = false, length = 60)
    private String blobUrl;

    @ManyToOne
    @JoinColumn(name = "Product_id", nullable = false)
    private Product product;
}
