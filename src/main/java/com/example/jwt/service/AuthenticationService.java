package com.example.jwt.service;

import com.example.jwt.dto.LoginUserDto;
import com.example.jwt.dto.RegisterUserDto;
import com.example.jwt.entity.User;
import com.example.jwt.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
        UserRepository userRepository,
        AuthenticationManager authenticationManager,
        PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(final RegisterUserDto registerUserDto) {
        final User user = new User();
        user.setFullName(registerUserDto.getFullname());
        user.setEmail(registerUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        return this.userRepository.save(user);
    }

    public User authenticate(LoginUserDto loginUserDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                		loginUserDto.getEmail(),
                		loginUserDto.getPassword()
                )
        );

        return this.userRepository.findByEmail(loginUserDto.getEmail())
                .orElseThrow(()->new UsernameNotFoundException("User Email Not Found..."));
    }
}