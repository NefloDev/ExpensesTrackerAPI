package com.neflodev.expensestrackerapi.web;

import com.neflodev.expensestrackerapi.dto.AccountCreateParams;
import com.neflodev.expensestrackerapi.dto.AccountDto;
import com.neflodev.expensestrackerapi.dto.IdBody;
import com.neflodev.expensestrackerapi.service.AccountService;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<List<AccountDto>> postRetrieveUserAccounts(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        return ResponseEntity.ok(service.retrieveUserAccounts(currentUserName));
    }

    @GetMapping("/currencies")
    public ResponseEntity<List<String>> getRetrieveAvailableCurrencies(){
        return ResponseEntity.ok(service.retrieveAvailableCurrencies());
    }

    @PostMapping("/")
    public ResponseEntity<IdBody> postCreateAccount(@RequestBody AccountCreateParams params){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        return ResponseEntity.ok(service.createAccount(currentUserName, params));
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathParam("accountId") Long accountId){
        service.deleteAccount(accountId);
        return ResponseEntity.ok().build();
    }

}
