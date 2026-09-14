package dev.sorokin.eventmanager.service;

import dev.sorokin.eventmanager.dto.request.UserCredentials;
import dev.sorokin.eventmanager.dto.response.JwtResponse;
import dev.sorokin.eventmanager.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public JwtResponse authenticateUser(UserCredentials userCredentials) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userCredentials.login(),
                userCredentials.password()
        ));
        String login = authentication.getName();
        return new JwtResponse(jwtService.generateToken(login));
    }
}