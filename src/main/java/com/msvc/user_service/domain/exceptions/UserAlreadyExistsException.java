package com.msvc.user_service.domain.exceptions;

public class UserAlreadyExistsException extends BusinessException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
