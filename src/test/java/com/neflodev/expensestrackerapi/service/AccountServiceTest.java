package com.neflodev.expensestrackerapi.service;

import com.neflodev.expensestrackerapi.dto.account.AccountCreateParams;
import com.neflodev.expensestrackerapi.dto.account.AccountDto;
import com.neflodev.expensestrackerapi.exception.custom.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AccountServiceTest {

    @Autowired
    AccountService service;

    @Test
    void retrieveUserAccountsExisting() {
        List<AccountDto> accounts = service.retrieveUserAccounts("User");

        assertNotNull(accounts);
        assertFalse(accounts.isEmpty());
    }

    @Test
    void retrieveAccountsEmpty() {
        List<AccountDto> accounts = service.retrieveUserAccounts("UserNoAccounts");

        assertNotNull(accounts);
        assertTrue(accounts.isEmpty());
    }

    @Test
    void retrieveAvailableCurrencies() {
        List<String> currencies = service.retrieveAvailableCurrencies();

        assertNotNull(currencies);
        assertFalse(currencies.isEmpty());
    }

    @Test
    void createAccount() {
        String username = "UserNoAccounts";
        AccountCreateParams params = new AccountCreateParams("NewAccount", "EUR");

        assertNotNull(service.createAccount(username, params));
    }

    @Test
    void deleteAccount() {
        assertDoesNotThrow(() -> service.deleteAccount(998L));
    }

    @Test
    void deleteAccountNotFound() {
        assertThrows(NotFoundException.class, () -> service.deleteAccount(2L));
    }
}