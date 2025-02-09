package com.exporcar.authtest.service;

import com.exporcar.authtest.domain.dto.UserDto;
import com.exporcar.authtest.domain.dto.response.JwtToken;
import com.exporcar.authtest.domain.entity.Member;
import com.exporcar.authtest.jwt.JwtTokenProvider;
import com.exporcar.authtest.jwt.JwtType;
import com.exporcar.authtest.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public JwtToken login(String username, String password) {
        Optional<Member> user = userRepository.findByUsername(username);

        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            String accessToken = jwtTokenProvider.generateToken(username, JwtType.ACCESS_TOKEN);
            String refreshToken = jwtTokenProvider.generateToken(username, JwtType.REFRESH_TOKEN);
            return new JwtToken(accessToken, refreshToken);
        }

        throw new RuntimeException("Invalid username or password");
    }

    @Transactional
    public void register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        Member user = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();
        userRepository.save(user);
    }


    public UserDto getUserByUsername(String username) {
        Member user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new UserDto(user.getId(), user.getUsername(), user.getPassword());
    }
}
