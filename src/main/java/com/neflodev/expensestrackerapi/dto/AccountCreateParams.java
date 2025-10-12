package com.neflodev.expensestrackerapi.dto;

public record AccountCreateParams(
        String accountName,
        String currency
) {
}
