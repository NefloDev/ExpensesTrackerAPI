package com.neflodev.expensestrackerapi.exception.custom;

public class UsernameInUseException extends CustomException {
    public UsernameInUseException(String message) {
        super(message);
    }

    public UsernameInUseException() {
        super("That username is already in use");
    }

}
