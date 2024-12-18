package com.twofullmoon.howmuchmarket.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignupRequestDTO {
	private String id;
	private String pw;
	private String name;
	private String accountNumber;
	private LocationDTO location;
}