package com.msvc.user_service.domain.exceptions;

import java.util.UUID;

public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException(String userId) {
        super(String.format("User %s not found in database", userId));
    }

}
