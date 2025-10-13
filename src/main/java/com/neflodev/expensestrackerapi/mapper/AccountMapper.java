package com.neflodev.expensestrackerapi.mapper;

import com.neflodev.expensestrackerapi.dto.account.AccountDto;
import com.neflodev.expensestrackerapi.model.AccountEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountDto accountToDto(AccountEntity source);

}
