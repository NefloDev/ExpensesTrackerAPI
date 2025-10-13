package com.neflodev.expensestrackerapi.service;

import com.neflodev.expensestrackerapi.constants.ExceptionsConst;
import com.neflodev.expensestrackerapi.dto.account.AccountCreateParams;
import com.neflodev.expensestrackerapi.dto.account.AccountDto;
import com.neflodev.expensestrackerapi.dto.general.IdBody;
import com.neflodev.expensestrackerapi.exception.custom.NotFoundException;
import com.neflodev.expensestrackerapi.mapper.AccountMapper;
import com.neflodev.expensestrackerapi.model.AccountEntity;
import com.neflodev.expensestrackerapi.model.UserEntity;
import com.neflodev.expensestrackerapi.repository.AccountEntityRepository;
import com.neflodev.expensestrackerapi.repository.UserEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class AccountService {

    private final AccountEntityRepository repository;
    private final AccountMapper mapper;
    private final UserEntityRepository userRepo;

    @Autowired
    public AccountService(AccountEntityRepository repository, AccountMapper mapper, UserEntityRepository userRepo) {
        this.repository = repository;
        this.mapper = mapper;
        this.userRepo = userRepo;
    }

    public List<AccountDto> retrieveUserAccounts(String username){
        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(() -> ExceptionsConst.USER_NOT_FOUND_EXCEPTION);

        List<AccountEntity> userAccounts = repository.findByUser_Id(user.getId());
        if (userAccounts.isEmpty()) {
            return new ArrayList<>();
        }

        return userAccounts.stream().map(mapper::accountToDto).toList();
    }

    public List<String> retrieveAvailableCurrencies(){
        Set<Currency> availableCurrencies = Currency.getAvailableCurrencies();

        return availableCurrencies.stream().map(Currency::getCurrencyCode).toList();
    }
    
    public IdBody createAccount(String username, AccountCreateParams params){
        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(() -> ExceptionsConst.USER_NOT_FOUND_EXCEPTION);
        Currency currencyName = Currency.getInstance(params.currency());

        AccountEntity account = new AccountEntity();
        account.setAccountName(params.accountName());
        account.setCurrency(currencyName.getCurrencyCode());
        account.setUser(user);

        return new IdBody(repository.save(account).getId());
    }

    public void deleteAccount(Long accountId){
        if (!repository.existsById(accountId)){
            throw new NotFoundException("Requested account doesn't exist");
        }

        repository.deleteById(accountId);
    }

}
