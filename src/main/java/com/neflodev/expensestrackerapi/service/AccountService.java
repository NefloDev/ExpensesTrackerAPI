package com.neflodev.expensestrackerapi.service;

import com.neflodev.expensestrackerapi.dto.AccountCreateParams;
import com.neflodev.expensestrackerapi.dto.AccountDto;
import com.neflodev.expensestrackerapi.dto.IdBody;
import com.neflodev.expensestrackerapi.exception.custom.NotFoundException;
import com.neflodev.expensestrackerapi.mapper.AccountMapper;
import com.neflodev.expensestrackerapi.model.Account;
import com.neflodev.expensestrackerapi.model.User;
import com.neflodev.expensestrackerapi.repository.AccountRepository;
import com.neflodev.expensestrackerapi.repository.UserRepository;
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

    private final AccountRepository repository;
    private final AccountMapper mapper;
    private final UserRepository userRepo;

    @Autowired
    public AccountService(AccountRepository repository, AccountMapper mapper, UserRepository userRepo) {
        this.repository = repository;
        this.mapper = mapper;
        this.userRepo = userRepo;
    }

    public List<AccountDto> retrieveUserAccounts(String username){
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Requested user was not found"));

        List<Account> userAccounts = repository.findByUser_Id(user.getId());
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
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Requested user was not found"));
        Currency currencyName = Currency.getInstance(params.currency());

        Account account = new Account();
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
