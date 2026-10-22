package com.example.experiment1_spring_security.auth;

import com.example.experiment1_spring_security.auth.request.AuthenticationRequestDto;
import com.example.experiment1_spring_security.auth.request.RegistrationRequestDto;
import com.example.experiment1_spring_security.auth.response.AuthenticationResponseDto;
import com.example.experiment1_spring_security.email.EmailService;
import com.example.experiment1_spring_security.email.EmailTemplateName;
import com.example.experiment1_spring_security.role.RoleRepository;
import com.example.experiment1_spring_security.security.JwtService;
import com.example.experiment1_spring_security.user.Token;
import com.example.experiment1_spring_security.user.TokenRepository;
import com.example.experiment1_spring_security.user.User;
import com.example.experiment1_spring_security.user.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    @Override
    public void register(RegistrationRequestDto dto) throws MessagingException {
        var userRole = roleRepository.findByName("USER")
                .orElseThrow(()-> new EntityNotFoundException("Role USER not found"));
        var user = User.builder()
                .firstname(dto.firstname())
                .lastname(dto.lastname())
                .email(dto.email())
                .password(encoder.encode(dto.password()))
                .accountLocked(false)
                .createdDate(LocalDateTime.now())
                .enabled(false)
                .roles(List.of(userRole))
                .build();
        userRepository.save(user);
        sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);

        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account Activation"
        );
    }

    private String generateAndSaveActivationToken(User user) {
        String generatedToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generatedToken)
                .user(user)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String character = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++){
            int randomIndex = secureRandom.nextInt(character.length());
            codeBuilder.append(character.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }

    @Override
    public AuthenticationResponseDto authenticate(AuthenticationRequestDto requestDto) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.email(),
                        requestDto.password()
                )
        );
        var claims = new HashMap<String, Object>();
        var user = ((User) auth.getPrincipal());
        claims.put("fullName", user.fullName());
        var jwtToken = jwtService.generateToken(claims, user);
        return AuthenticationResponseDto.builder()
                .token(jwtToken)
                .build();
    }


    @Override
//    @Transactional
    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                // todo exception has to be defined
                .orElseThrow(()-> new RuntimeException("Invalid token"));
        if (LocalDateTime.now().isAfter(savedToken.getExpiredAt())){
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token has expired. A new token has been sent to the same email address");
        }
        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }
}
