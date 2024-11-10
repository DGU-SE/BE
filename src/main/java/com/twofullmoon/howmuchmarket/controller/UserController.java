package com.twofullmoon.howmuchmarket.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twofullmoon.howmuchmarket.dto.LocationDTO;
import com.twofullmoon.howmuchmarket.dto.UserDTO;
import com.twofullmoon.howmuchmarket.entity.Location;
import com.twofullmoon.howmuchmarket.entity.User;
import com.twofullmoon.howmuchmarket.service.LocationService;
import com.twofullmoon.howmuchmarket.service.UserService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/user")
public class UserController {
	private final UserService userService;
	private final LocationService locationService;

	public UserController(UserService userService, LocationService locationService) {
		this.userService = userService;
		this.locationService = locationService;
	}

	@GetMapping("/{id}")
	public String getUserById(@PathVariable String id) {
		return "hi" + id + "1";
	}

	@Transactional
	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO) {
		LocationDTO locationDTO = userDTO.getLocation();
		User createdUser;

		if (locationDTO == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		Optional<Location> location = Optional.empty();
		if (locationDTO.getId() != null) {
			location = locationService.findLocationById(locationDTO.getId());
		}
		
		if (location.isPresent()) {
			createdUser = User.builder()
					.id(userDTO.getId())
					.pw(userDTO.getPw())
					.name(userDTO.getName())
					.accountNumber(userDTO.getAccountNumber())
					.location(location.get())
					.build();
		} else {
			Location locationData = Location.builder()
					.longitude(locationDTO.getLongitude())
					.latitude(locationDTO.getLatitude())
					.zipcode(locationDTO.getZipcode())
					.address(locationDTO.getAddress())
					.addressDetail(locationDTO.getAddressDetail())
					.build();

			locationService.createLocation(locationData);

			createdUser = User.builder()
					.id(userDTO.getId())
					.pw(userDTO.getPw())
					.name(userDTO.getName())
					.accountNumber(userDTO.getAccountNumber())
					.location(locationData)
					.build();
		}

		createdUser = userService.createUser(createdUser);

		return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
	}
}
