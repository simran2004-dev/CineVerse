package com.cineverse.auth.service;

import com.cineverse.auth.dto.AuthResponse;
import com.cineverse.auth.dto.LoginDTO;
import com.cineverse.auth.dto.RegisterDTO;
import com.cineverse.auth.entity.Role;
import com.cineverse.auth.entity.User;
import com.cineverse.auth.exception.CustomException;
import com.cineverse.auth.repository.UserRepository;
import com.cineverse.auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse register(RegisterDTO registerDTO) {
        if (userRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
            throw new CustomException("Email already exists");
        }

        User user = User.builder()
                .name(registerDTO.getName())
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .role(registerDTO.getRole() != null ? registerDTO.getRole() : Role.USER)
                .build();

        user = userRepository.save(user);
        String token = jwtUtil.generateToken(user.getId(), user.getRole().name());

        return new AuthResponse(token, user.getId(), user.getRole().name());
    }

    public AuthResponse login(LoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new CustomException("Invalid email or password"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new CustomException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getRole().name());
        return new AuthResponse(token, user.getId(), user.getRole().name());
    }
}
