package com.bank.security.auth;

import com.bank.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    /**
     * User logging and generating JWT
     */
    public String login(String login, String password) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login, password)
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            return jwtService.generateToken(userDetails);

        } catch (AuthenticationException ex) {
            throw new RuntimeException("Invalid login or password");
        }
    }
}
