package com.example.experiment1_spring_security.auth;

import com.example.experiment1_spring_security.auth.request.AuthenticationRequestDto;
import com.example.experiment1_spring_security.auth.request.RegistrationRequestDto;
import com.example.experiment1_spring_security.auth.response.AuthenticationResponseDto;
import jakarta.mail.MessagingException;

public interface AuthService {
    void register(RegistrationRequestDto dto) throws MessagingException;
    AuthenticationResponseDto authenticate(AuthenticationRequestDto authenticationDto);
    void activateAccount(String token) throws MessagingException;
}
