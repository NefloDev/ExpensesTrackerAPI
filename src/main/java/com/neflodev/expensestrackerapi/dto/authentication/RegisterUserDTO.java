package com.neflodev.expensestrackerapi.dto.authentication;

public record RegisterUserDTO(
        String username,
        String password,
        String name,
        String surname
) {
}
