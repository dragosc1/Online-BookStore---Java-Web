package com.bookstore.controller;

import com.bookstore.dto.request.LoginRequestDto;
import com.bookstore.dto.request.RegisterRequestDto;
import com.bookstore.dto.response.AuthResponseDto;
import com.bookstore.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(
        name = "Authentication",
        description = "Endpoints for user login and registration"
)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // ========= LOGIN =========
    @Operation(
            summary = "User login",
            description = "Authenticate a user and return a JWT token"
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto request) {
        String token = authService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(new AuthResponseDto(token));
    }

    // ========= REGISTER =========
    @Operation(
            summary = "User registration",
            description = "Register a new user and return a JWT token"
    )
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody RegisterRequestDto request) {
        String token = authService.register(request);
        return ResponseEntity.ok(new AuthResponseDto(token));
    }
}
