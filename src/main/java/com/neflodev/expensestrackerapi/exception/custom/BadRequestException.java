package com.neflodev.expensestrackerapi.exception.custom;

public class BadRequestException extends CustomException {
    public BadRequestException(String message) {
        super(message);
    }
}
