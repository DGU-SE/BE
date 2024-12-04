package com.twofullmoon.howmuchmarket.mapper;

import com.twofullmoon.howmuchmarket.dto.UserResponseDTO;
import com.twofullmoon.howmuchmarket.dto.UserSignupRequestDTO;
import com.twofullmoon.howmuchmarket.entity.User;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

	private final LocationMapper locationMapper;
	private final PasswordEncoder passwordEncoder;

	public UserMapper(LocationMapper locationMapper, PasswordEncoder passwordEncoder) {
		this.locationMapper = locationMapper;
		this.passwordEncoder = passwordEncoder;
	}

	public UserResponseDTO toResponseDTO(User user) {
		UserResponseDTO dto = new UserResponseDTO();
		dto.setId(user.getId());
		dto.setName(user.getName());
		dto.setAccountNumber(user.getAccountNumber());
		dto.setLocation(locationMapper.toDTO(user.getLocation()));
		return dto;
	}

	public User toEntity(UserSignupRequestDTO dto) {
		return User.builder()
				.id(dto.getId())
				.pw(passwordEncoder.encode(dto.getPw()))
				.name(dto.getName())
				.accountNumber(dto.getAccountNumber())
				.location(locationMapper.toEntity(dto.getLocation()))
				.build();
	}
}