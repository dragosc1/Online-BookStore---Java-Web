package com.bookstore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request payload for user login")
public class LoginRequestDto {

    @NotBlank
    @Schema(description = "Username of the user", example = "john_doe", required = true)
    private String username;

    @NotBlank
    @Schema(description = "Password of the user", example = "P@ssword123", required = true)
    private String password;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
