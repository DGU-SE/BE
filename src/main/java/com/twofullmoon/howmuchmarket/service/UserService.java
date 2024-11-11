package com.twofullmoon.howmuchmarket.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.twofullmoon.howmuchmarket.dto.LocationDTO;
import com.twofullmoon.howmuchmarket.dto.UserResponseDTO;
import com.twofullmoon.howmuchmarket.dto.UserSignupRequestDTO;
import com.twofullmoon.howmuchmarket.entity.Location;
import com.twofullmoon.howmuchmarket.entity.User;
import com.twofullmoon.howmuchmarket.exception.InvalidUserInformationException;
import com.twofullmoon.howmuchmarket.exception.UserAlreadyExistsException;
import com.twofullmoon.howmuchmarket.exception.UserLocationNullException;
import com.twofullmoon.howmuchmarket.mapper.LocationMapper;
import com.twofullmoon.howmuchmarket.mapper.UserMapper;
import com.twofullmoon.howmuchmarket.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final LocationService locationService;
	private final UserMapper userMapper;
	private final LocationMapper locationMapper;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, LocationService locationService, UserMapper userMapper,
			LocationMapper locationMapper, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.locationService = locationService;
		this.userMapper = userMapper;
		this.locationMapper = locationMapper;
		this.passwordEncoder = passwordEncoder;
	}

	public Optional<User> findUserById(String id) {
		return userRepository.findById(id);
	}

	public User createUser(User user) {
		if (userRepository.existsById(user.getId())) {
			throw new UserAlreadyExistsException("User with ID " + user.getId() + " already exists.");
		}
		return userRepository.save(user);
	}

	@Transactional
	public UserResponseDTO createUser(UserSignupRequestDTO userDTO) {
		LocationDTO locationDTO = userDTO.getLocation();
		if (locationDTO == null) {
			throw new UserLocationNullException("User location cannot be null.");
		}

		Location location;
		if (locationDTO.getId() != null) {
			location = locationService.findLocationById(locationDTO.getId())
					.orElseGet(() -> locationService.createLocation(locationMapper.toEntity(locationDTO)));
		} else {
			location = locationService.createLocation(locationMapper.toEntity(locationDTO));
		}

		User createdUser = userMapper.toEntity(userDTO);
		createdUser.setLocation(location);
		createdUser.setPw(passwordEncoder.encode(userDTO.getPw()));
		createdUser = createUser(createdUser);

		return userMapper.toResponseDTO(createdUser);
	}
	
	@Transactional
	public UserResponseDTO setUserLocation(String userId, LocationDTO locationDTO) {
		Optional<User> user = findUserById(userId);
		
		if (!user.isPresent()) {
			throw new InvalidUserInformationException("user id " + userId + " was not found.");
		}
		
		User userEntity = user.get();
		Location userLocation = userEntity.getLocation();
		
		Location newLocation = locationMapper.toEntity(locationDTO);
		newLocation.setId(userLocation.getId());
		locationService.saveLocation(newLocation);
		
		userEntity.setLocation(newLocation);
		
		return userMapper.toResponseDTO(userEntity);
	}
}
