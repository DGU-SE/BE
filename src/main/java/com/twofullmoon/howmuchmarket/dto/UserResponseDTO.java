package com.twofullmoon.howmuchmarket.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {
	private String id;
	private String name;
	private String accountNumber;
	private LocationDTO location;
}