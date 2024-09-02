package com.msvc.user_service.domain.exceptions;

public class NameAlreadyExistsException extends UserAlreadyExistsException{

    public NameAlreadyExistsException(String name) {
        super(String.format("There is already a user %s saved in the database", name));
    }


}
