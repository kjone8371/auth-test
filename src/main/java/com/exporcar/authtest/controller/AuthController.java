package com.exporcar.authtest.controller;

import com.exporcar.authtest.domain.dto.UserDto;
import com.exporcar.authtest.domain.dto.response.JwtToken;
import com.exporcar.authtest.jwt.JwtTokenProvider;
import com.exporcar.authtest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        userService.register(request.get("username"), request.get("password"));
        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        JwtToken jwtToken = userService.login(request.get("username"), request.get("password"));
        return ResponseEntity.ok(Map.of(
                "code", "OK",
                "status", 200,
                "data", jwtToken
        ));
    }

    // 🔹 현재 로그인한 사용자 정보 가져오기
    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
        }

        String username = authentication.getName();
        UserDto userDto = userService.getUserByUsername(username);
        return ResponseEntity.ok(Map.of("code", "OK", "status", 200, "data", userDto));
    }

}
