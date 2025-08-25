package com.amitbashan.career_hub.dto;

import jakarta.validation.constraints.*;

public class StudentLoginRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 3)
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9_]*$", message = "Username must start with a letter than a sequence of alphanumeric or underscore characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 20, message = "Password must be atleast 8 characters and at most 20 characters")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
