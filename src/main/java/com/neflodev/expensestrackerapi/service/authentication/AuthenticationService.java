package com.neflodev.expensestrackerapi.service.authentication;

import com.neflodev.expensestrackerapi.dto.authentication.LoginUserDTO;
import com.neflodev.expensestrackerapi.dto.authentication.RegisterUserDTO;
import com.neflodev.expensestrackerapi.model.User;
import com.neflodev.expensestrackerapi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepo, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public boolean isUserRegistered(RegisterUserDTO registerDTO){
        return userRepo.existsByUsername(registerDTO.username());
    }

    public User signup(RegisterUserDTO registerDTO) {
        User user = User.builder()
                .name(registerDTO.name())
                .surname(registerDTO.surname())
                .username(registerDTO.username())
                .password(passwordEncoder.encode(registerDTO.password()))
                .build();

        return userRepo.save(user);
    }

    public User authenticate(LoginUserDTO loginDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password())
        );

        return userRepo.findByUsername(loginDTO.username()).orElseThrow();
    }

}
