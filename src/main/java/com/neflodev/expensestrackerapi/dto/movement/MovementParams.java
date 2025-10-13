package com.neflodev.expensestrackerapi.dto.movement;

import com.neflodev.expensestrackerapi.constants.enums.MovementType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MovementParams {

    private Long id;
    private String accountName;
    private BigDecimal amount;
    private String category;
    private MovementType movementType;
    private String destinationAccount;
    private String comment;

}
