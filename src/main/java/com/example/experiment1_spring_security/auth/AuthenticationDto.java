package com.example.experiment1_spring_security.auth;

public record AuthenticationDto(
        String email,
        String password
) {}
