package com.neflodev.expensestrackerapi.dto;

public record LoginResponse(
        String token,
        long expiresOn
) {
}
