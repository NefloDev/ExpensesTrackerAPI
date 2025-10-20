package com.neflodev.expensestrackerapi.dto.movement;

import java.util.List;

public record MovementFilters(
        List<String> movementTypes,
        List<String> categories
) {
}
