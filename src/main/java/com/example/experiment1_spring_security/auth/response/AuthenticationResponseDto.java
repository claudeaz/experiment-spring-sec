package com.example.experiment1_spring_security.auth.response;

import lombok.Builder;

@Builder
public record AuthenticationResponseDto(
        String token
) {
}
