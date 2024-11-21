package com.twofullmoon.howmuchmarket.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class ProductRequestDTO {
    private String name;
    private Integer price;
    private LocalDateTime dealTime;
    private LocationDTO locationDTO;
    private String productDetail;
    private Boolean onAuction;
    private String userId;
//    private UserResponseDTO userResponseDTO;
//	public ProductRequestDTO getProductRequestDTO() {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
