package com.neflodev.expensestrackerapi.mapper;

import com.neflodev.expensestrackerapi.dto.AccountDto;
import com.neflodev.expensestrackerapi.model.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountDto accountToDto(Account source);

}
