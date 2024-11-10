package com.twofullmoon.howmuchmarket.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twofullmoon.howmuchmarket.dto.UserLoginRequestDTO;
import com.twofullmoon.howmuchmarket.dto.UserLoginResponseDTO;
import com.twofullmoon.howmuchmarket.entity.User;
import com.twofullmoon.howmuchmarket.exception.InvalidUserInformationException;
import com.twofullmoon.howmuchmarket.service.UserService;
import com.twofullmoon.howmuchmarket.util.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final UserService userService;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	public AuthController(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
	}

	@PostMapping("/login")
	public ResponseEntity<UserLoginResponseDTO> login(@RequestBody UserLoginRequestDTO request) {
		if (request.getPassword() == null || request.getId() == null) {
	        return ResponseEntity.badRequest().body(null);
	    }
		
		User user = userService.findUserById(request.getId())
				.orElseThrow(() -> new InvalidUserInformationException("Invalid username or password"));

		if (passwordEncoder.matches(request.getPassword(), user.getPw())) {
			String token = jwtUtil.generateToken(user.getId());
			return ResponseEntity.ok(new UserLoginResponseDTO(token));
		} else {
			return ResponseEntity.status(401)
					.build();
		}
	}
}
