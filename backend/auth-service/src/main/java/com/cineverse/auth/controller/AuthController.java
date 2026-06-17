package com.cineverse.auth.controller;

import com.cineverse.auth.dto.AuthResponse;
import com.cineverse.auth.dto.LoginDTO;
import com.cineverse.auth.dto.RegisterDTO;
import com.cineverse.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterDTO registerDTO) {
        AuthResponse response = authService.register(registerDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginDTO loginDTO) {
        AuthResponse response = authService.login(loginDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        // In a stateless JWT setup, logout is typically handled client-side by dropping the token.
        // Or we could implement a token blacklist here.
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        // Placeholder for forgot password logic
        return ResponseEntity.ok("Password reset link sent (placeholder)");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
         // Placeholder for reset password logic
        return ResponseEntity.ok("Password reset successfully (placeholder)");
    }
}
