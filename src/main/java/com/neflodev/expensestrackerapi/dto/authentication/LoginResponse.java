package com.neflodev.expensestrackerapi.dto.authentication;

public record LoginResponse(
        String token,
        long expiresOn
) {
}
