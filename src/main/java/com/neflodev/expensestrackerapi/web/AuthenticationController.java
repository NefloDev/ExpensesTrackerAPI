package com.neflodev.expensestrackerapi.web;

import com.neflodev.expensestrackerapi.dto.LoginResponse;
import com.neflodev.expensestrackerapi.dto.LoginUserDTO;
import com.neflodev.expensestrackerapi.dto.RegisterUserDTO;
import com.neflodev.expensestrackerapi.exception.custom.UsernameInUseException;
import com.neflodev.expensestrackerapi.model.User;
import com.neflodev.expensestrackerapi.service.AuthenticationService;
import com.neflodev.expensestrackerapi.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> registerUser(@RequestBody RegisterUserDTO registerDTO) {
        if (authenticationService.isUserRegistered(registerDTO)){
            throw new UsernameInUseException();
        }

        User registeredUser = authenticationService.signup(registerDTO);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginUserDTO loginDTO) {
        User authenticatedUser = authenticationService.authenticate(loginDTO);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }

}
