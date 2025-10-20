package com.neflodev.expensestrackerapi.dto.movement;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record MovementRequestBody(
        String accountName,
        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate startDate,
        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate endDate
) {}
