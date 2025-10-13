package com.neflodev.expensestrackerapi.dto.general;

import java.time.LocalDateTime;

public record CustomExceptionResponse (
    String message,
    Integer statusCode,
    LocalDateTime timestamp
) {}
