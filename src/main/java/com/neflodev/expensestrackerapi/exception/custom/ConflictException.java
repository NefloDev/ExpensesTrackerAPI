package com.neflodev.expensestrackerapi.exception.custom;

public class ConflictException extends CustomException {
    public ConflictException(String message) {
        super(message);
    }

    public ConflictException() {
        super("That username is already in use");
    }

}
