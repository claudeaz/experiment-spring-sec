package com.example.experiment1_spring_security.auth;

import com.example.experiment1_spring_security.auth.request.AuthenticationRequestDto;
import com.example.experiment1_spring_security.auth.request.RegistrationRequestDto;
import com.example.experiment1_spring_security.auth.response.AuthenticationResponseDto;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegistrationRequestDto request) throws MessagingException {
        authService.register(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@Valid @RequestBody AuthenticationRequestDto requestDto){
        return ResponseEntity.ok(authService.authenticate(requestDto));
    }

    @GetMapping("/activate-account")
    public ResponseEntity<Void> activateAccount(String token) throws MessagingException {
        authService.activateAccount(token);
        return ResponseEntity.ok().build();
    }
}
