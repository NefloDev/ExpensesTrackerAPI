package com.neflodev.expensestrackerapi.dto.movement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovementDto {

    private String amount;
    private String category;
    private String comment;
    private String currencySymbol;

}
