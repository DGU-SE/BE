package com.twofullmoon.howmuchmarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.twofullmoon.howmuchmarket.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {

}
