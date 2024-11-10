package com.twofullmoon.howmuchmarket.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twofullmoon.howmuchmarket.entity.Location;
import com.twofullmoon.howmuchmarket.service.LocationService;

@RestController
@RequestMapping("/api/location")
public class LocationController {
	private final LocationService locationService;

	public LocationController(LocationService locationService) {
		this.locationService = locationService;
	}

	@PostMapping
	public ResponseEntity<Location> createUser(@RequestBody Location location) {
		Location createdUser = locationService.createLocation(location);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
	}
}
