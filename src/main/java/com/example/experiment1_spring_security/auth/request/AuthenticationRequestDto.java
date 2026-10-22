package com.example.experiment1_spring_security.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AuthenticationRequestDto(
        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 8, max = 56)
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*\\W).*$", message = "Wrong password format")
        String password
) {
}
