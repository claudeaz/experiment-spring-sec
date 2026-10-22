package com.example.experiment1_spring_security.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BusinessErrorCodes {
    NO_CODE(0, "No code", HttpStatus.NOT_IMPLEMENTED),
    INCORRECT_CURRENT_PASSWORD(300, "Current password is incorrect", HttpStatus.BAD_REQUEST),
    NEW_PASSWORD_DOES_NOT_PASSWORD(301, "The new password does not match", HttpStatus.BAD_REQUEST),
    ACCOUNT_LOCKED(302, "User account is locked", HttpStatus.FORBIDDEN),
    ACCOUNT_DISABLED(302, "User account is disabled", HttpStatus.FORBIDDEN),
    BAD_CREDENTIALS(3030, "Login and/ or password is incorrecte", HttpStatus.FORBIDDEN)
    ;

    private final int code;
    private final HttpStatus status;
    private final String message;

    BusinessErrorCodes(int code, String message, HttpStatus status) {
        this.code = code;
        this.status = status;
        this.message = message;
    }
}
