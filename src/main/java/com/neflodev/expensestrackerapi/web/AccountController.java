package com.neflodev.expensestrackerapi.web;

import com.neflodev.expensestrackerapi.dto.account.AccountCreateParams;
import com.neflodev.expensestrackerapi.dto.account.AccountDto;
import com.neflodev.expensestrackerapi.dto.general.IdBody;
import com.neflodev.expensestrackerapi.service.AccountService;
import com.neflodev.expensestrackerapi.util.CustomUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("accounts/")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @GetMapping("/me")
    public ResponseEntity<List<AccountDto>> getRetrieveUserAccounts(){
        String currentUserName = CustomUtils.retrieveSessionUsername();

        return ResponseEntity.ok(service.retrieveUserAccounts(currentUserName));
    }

    @GetMapping("/currencies")
    public ResponseEntity<List<String>> getRetrieveAvailableCurrencies(){
        return ResponseEntity.ok(service.retrieveAvailableCurrencies());
    }

    @PostMapping("/")
    public ResponseEntity<IdBody> postCreateAccount(@RequestBody AccountCreateParams params){
        String currentUserName = CustomUtils.retrieveSessionUsername();

        return ResponseEntity.ok(service.createAccount(currentUserName, params));
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable("accountId") Long accountId){
        service.deleteAccount(accountId);
        return ResponseEntity.ok().build();
    }

}
