package com.cineverse.auth.service;

import com.cineverse.auth.dto.AuthResponse;
import com.cineverse.auth.dto.LoginDTO;
import com.cineverse.auth.dto.RegisterDTO;
import com.cineverse.auth.entity.Role;
import com.cineverse.auth.entity.User;
import com.cineverse.auth.exception.CustomException;
import com.cineverse.auth.repository.UserRepository;
import com.cineverse.auth.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private RegisterDTO registerDTO;
    private LoginDTO loginDTO;
    private User savedUser;

    @BeforeEach
    void setUp() {
        registerDTO = new RegisterDTO();
        registerDTO.setEmail("test@cineverse.com");
        registerDTO.setPassword("password123");
        registerDTO.setFullName("Test User");

        loginDTO = new LoginDTO();
        loginDTO.setEmail("test@cineverse.com");
        loginDTO.setPassword("password123");

        savedUser = User.builder()
                .id(1L)
                .email("test@cineverse.com")
                .password("hashed_password")
                .fullName("Test User")
                .role(Role.USER)
                .build();
    }

    @Test
    void register_Success() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_password");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("mock.jwt.token");

        AuthResponse response = authService.register(registerDTO);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("mock.jwt.token");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_DuplicateEmail_ThrowsException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(savedUser));

        assertThatThrownBy(() -> authService.register(registerDTO))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void login_Success() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(savedUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("mock.jwt.token");

        AuthResponse response = authService.login(loginDTO);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("mock.jwt.token");
    }

    @Test
    void login_WrongPassword_ThrowsException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(savedUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThatThrownBy(() -> authService.login(loginDTO))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("Invalid");
    }

    @Test
    void login_UserNotFound_ThrowsException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(loginDTO))
                .isInstanceOf(CustomException.class);
    }
}
