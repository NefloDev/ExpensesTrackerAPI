package com.neflodev.expensestrackerapi.service;

import com.neflodev.expensestrackerapi.dto.authentication.LoginUserDTO;
import com.neflodev.expensestrackerapi.dto.authentication.RegisterUserDTO;
import com.neflodev.expensestrackerapi.service.authentication.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class AuthenticationServiceTest {

    @Autowired
    AuthenticationService service;

    @Test
    void testIsUserRegisteredTrue() {
        RegisterUserDTO temp = new RegisterUserDTO("User", "password", "User", "User");

        assertTrue(service.isUserRegistered(temp));
    }

    @Test
    void testIsUserRegisteredFalse() {
        RegisterUserDTO temp = new RegisterUserDTO("UserName2", "password2", "User2", "User2");

        assertFalse(service.isUserRegistered(temp));
    }

    @Test
    void signup() {
        RegisterUserDTO temp = new RegisterUserDTO("UserName3", "password3", "User3", "User3");

        assertNotNull(service.signup(temp));
        assertTrue(service.isUserRegistered(temp));
    }

    @Test
    void authenticate() {
        LoginUserDTO temp = new LoginUserDTO("User", "password");

        assertNotNull(service.authenticate(temp));
    }
}