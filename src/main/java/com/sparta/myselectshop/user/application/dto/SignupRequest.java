package com.sparta.myselectshop.user.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignupRequest(
    @NotBlank String username,
    @NotBlank String password,
    @Email @NotBlank String email,
    Boolean isAdmin,
    String adminToken) {}
