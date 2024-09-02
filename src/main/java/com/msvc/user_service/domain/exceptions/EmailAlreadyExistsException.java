package com.msvc.user_service.domain.exceptions;

public class EmailAlreadyExistsException extends UserAlreadyExistsException{

    public EmailAlreadyExistsException(String email) {
        super(String.format("There is already an email %s registered in the database", email));
    }
}
