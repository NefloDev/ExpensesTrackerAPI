package com.neflodev.expensestrackerapi.dto;

public record RegisterUserDTO(
        String username,
        String password,
        String name,
        String surname
) {
}
