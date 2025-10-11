package com.neflodev.expensestrackerapi.dto;

import java.time.LocalDateTime;

public record CustomExceptionResponse (
    String message,
    Integer statusCode,
    LocalDateTime timestamp
) { }
