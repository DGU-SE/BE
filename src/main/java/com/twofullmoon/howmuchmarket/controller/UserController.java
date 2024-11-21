package com.twofullmoon.howmuchmarket.controller;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.twofullmoon.howmuchmarket.dto.LocationDTO;
import com.twofullmoon.howmuchmarket.dto.UserResponseDTO;
import com.twofullmoon.howmuchmarket.dto.UserSignupRequestDTO;
import com.twofullmoon.howmuchmarket.mapper.UserMapper;
import com.twofullmoon.howmuchmarket.service.UserService;
import com.twofullmoon.howmuchmarket.util.JwtUtil;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, UserMapper userMapper, JwtUtil jwtUtil) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/{id}")
    public Optional<UserResponseDTO> getUserById(@PathVariable(name = "id") String id) {
        return userService.findUserById(id)
                .map(userMapper::toResponseDTO);
    }

    @GetMapping("/id/duplicate")
    public ResponseEntity<HashMap<String, Boolean>> checkDuplicateId(@RequestParam(name = "id") String id) {
        boolean isDuplicated = userService.findUserById(id).isPresent();

        HashMap<String, Boolean> response = new HashMap<>();
        response.put("duplicated", isDuplicated);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/join")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserSignupRequestDTO userDTO) {
        UserResponseDTO createdUserDTO = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdUserDTO);
    }

    @PatchMapping("/{userId}/location")
    public ResponseEntity<?> setUserResidence(@PathVariable(name = "userId") String userId,
                                              @RequestBody LocationDTO locationDTO, @RequestHeader("Authorization") String authorizationHeader) {

        String tokenUserId = jwtUtil.extractUserId(authorizationHeader.substring(7));

        if (!tokenUserId.equals(userId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("userId in token and parameter are not equal.");
        }

        UserResponseDTO modifiedUserDTO = userService.setUserLocation(tokenUserId, locationDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(modifiedUserDTO);
    }
}
