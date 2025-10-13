package com.neflodev.expensestrackerapi.dto.account;

public record AccountCreateParams(
        String accountName,
        String currency
) {
}
