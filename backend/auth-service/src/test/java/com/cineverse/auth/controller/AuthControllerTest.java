package com.cineverse.auth.controller;

import com.cineverse.auth.dto.AuthResponse;
import com.cineverse.auth.dto.LoginDTO;
import com.cineverse.auth.dto.RegisterDTO;
import com.cineverse.auth.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register_Returns201WithToken() throws Exception {
        RegisterDTO dto = new RegisterDTO();
        dto.setEmail("test@cineverse.com");
        dto.setPassword("password123");
        dto.setFullName("Test User");

        AuthResponse response = AuthResponse.builder()
                .token("mock.jwt.token")
                .email("test@cineverse.com")
                .role("USER")
                .build();

        when(authService.register(any(RegisterDTO.class))).thenReturn(response);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("mock.jwt.token"))
                .andExpect(jsonPath("$.email").value("test@cineverse.com"));
    }

    @Test
    void login_Returns200WithToken() throws Exception {
        LoginDTO dto = new LoginDTO();
        dto.setEmail("test@cineverse.com");
        dto.setPassword("password123");

        AuthResponse response = AuthResponse.builder()
                .token("mock.jwt.token")
                .email("test@cineverse.com")
                .role("USER")
                .build();

        when(authService.login(any(LoginDTO.class))).thenReturn(response);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock.jwt.token"));
    }
}
