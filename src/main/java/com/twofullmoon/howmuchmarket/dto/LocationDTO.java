package com.twofullmoon.howmuchmarket.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationDTO {
	private Long id;
	private Double longitude;
	private Double latitude;
	private String zipcode;
	private String address;
	private String addressDetail;
}