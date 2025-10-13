package com.neflodev.expensestrackerapi.constants;

import com.neflodev.expensestrackerapi.exception.custom.NotFoundException;

public abstract class ExceptionsConst {

    public static final NotFoundException USER_NOT_FOUND_EXCEPTION = new NotFoundException("Requested user was not found.");
    public static final NotFoundException ACCOUNT_NOT_FOUND_EXCEPTION = new NotFoundException("Requested account was not found.");
    public static final NotFoundException DESTINATION_ACCOUNT_NOT_FOUND_EXCEPTION = new NotFoundException("Requested destination account was not found.");
    public static final NotFoundException CATEGORY_NOT_FOUND_EXCEPTION = new NotFoundException("Requested category was not found.");
    public static final NotFoundException MOVEMENT_NOT_FOUND_EXCEPTION = new NotFoundException("Requested movement was not found.");

    private ExceptionsConst(){}
}
