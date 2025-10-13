package com.neflodev.expensestrackerapi.dto.account;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDto {

    private Long id;
    private String accountName;
    private String currency;
    private String currencySymbol;

}
