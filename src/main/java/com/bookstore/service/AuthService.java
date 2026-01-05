package com.bookstore.service;

import com.bookstore.dto.request.RegisterRequestDto;

public interface AuthService {

    /**
     * Authenticate user and return JWT token
     */
    String login(String username, String password);

    /**
     * Register a new user and return JWT token
     */
    String register(RegisterRequestDto request);
}
