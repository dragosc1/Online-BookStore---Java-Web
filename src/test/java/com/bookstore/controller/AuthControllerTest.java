package com.bookstore.controller;

import com.bookstore.dto.request.LoginRequestDto;
import com.bookstore.dto.request.RegisterRequestDto;
import com.bookstore.dto.response.AuthResponseDto;
import com.bookstore.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin() {
        // Arrange
        String username = "testuser";
        String password = "password";
        String token = "dummy-jwt-token";

        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        when(authService.login(username, password)).thenReturn(token);

        // Act
        ResponseEntity<AuthResponseDto> response = authController.login(loginRequest);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(token, response.getBody().getAccessToken());

        verify(authService, times(1)).login(username, password);
    }

    @Test
    void testRegister() {
        // Arrange
        RegisterRequestDto registerRequest = new RegisterRequestDto();
        registerRequest.setUsername("newuser");
        registerRequest.setPassword("newpassword");
        registerRequest.setEmail("newuser@example.com");

        String token = "dummy-jwt-token";

        when(authService.register(registerRequest)).thenReturn(token);

        // Act
        ResponseEntity<AuthResponseDto> response = authController.register(registerRequest);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(token, response.getBody().getAccessToken());

        verify(authService, times(1)).register(registerRequest);
    }
}
