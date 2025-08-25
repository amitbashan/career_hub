package com.amitbashan.career_hub.dto;

import jakarta.validation.ConstraintViolation;

import java.util.Set;

public record LoginResponse<T>(boolean success, String message, Set<ConstraintViolation<T>> violations, String authToken) {
}
