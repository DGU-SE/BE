package com.twofullmoon.howmuchmarket.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.twofullmoon.howmuchmarket.entity.User;
import com.twofullmoon.howmuchmarket.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public Optional<User> findUserById(String id) {
		return userRepository.findById(id);
	}

	public User createUser(User user) {
		return userRepository.save(user);
	}
}
