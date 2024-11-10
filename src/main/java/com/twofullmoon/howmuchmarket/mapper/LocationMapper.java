package com.twofullmoon.howmuchmarket.mapper;

import org.springframework.stereotype.Component;

import com.twofullmoon.howmuchmarket.dto.LocationDTO;
import com.twofullmoon.howmuchmarket.entity.Location;

@Component
public class LocationMapper {

	public LocationDTO toDTO(Location location) {
		LocationDTO dto = new LocationDTO();
		dto.setId(location.getId());
		dto.setLongitude(location.getLongitude());
		dto.setLatitude(location.getLatitude());
		dto.setZipcode(location.getZipcode());
		dto.setAddress(location.getAddress());
		dto.setAddressDetail(location.getAddressDetail());
		return dto;
	}

	public Location toEntity(LocationDTO dto) {
		Location location = new Location();
		location.setId(dto.getId());
		location.setLongitude(dto.getLongitude());
		location.setLatitude(dto.getLatitude());
		location.setZipcode(dto.getZipcode());
		location.setAddress(dto.getAddress());
		location.setAddressDetail(dto.getAddressDetail());
		return location;
	}
}
