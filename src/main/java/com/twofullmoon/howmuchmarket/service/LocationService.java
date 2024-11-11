package com.twofullmoon.howmuchmarket.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.twofullmoon.howmuchmarket.entity.Location;
import com.twofullmoon.howmuchmarket.repository.LocationRepository;

@Service
public class LocationService {
	private final LocationRepository locationRepository;
	
	public LocationService(LocationRepository locationRepository) {
		this.locationRepository = locationRepository;
	}
	
	public Location createLocation(Location location) {
		return locationRepository.save(location);
	}
	
	public Location saveLocation(Location location) {
		return locationRepository.save(location);
	}
	
	public Optional<Location> findLocationById(Long id) {
		return locationRepository.findById(id);
	}
}
