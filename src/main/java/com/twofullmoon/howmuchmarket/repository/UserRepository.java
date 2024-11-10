package com.twofullmoon.howmuchmarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.twofullmoon.howmuchmarket.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
	
}
