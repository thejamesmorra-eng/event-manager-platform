package dev.sorokin.eventmanager.service;

import dev.sorokin.eventmanager.dto.request.UserCredentials;
import dev.sorokin.eventmanager.dto.response.JwtResponse;
import dev.sorokin.eventmanager.entity.UserEntity;
import dev.sorokin.eventmanager.repository.UserRepository;
import dev.sorokin.eventmanager.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtResponse authenticateUser(UserCredentials userCredentials) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userCredentials.login(),
                userCredentials.password()
        ));
        String login = authentication.getName();
        UserEntity userEntity = userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User with login '" + login + "' was not found"));

        String jwt = jwtService.generateToken(userEntity.getId(), login, userEntity.getRole());
        return new JwtResponse(jwt);
    }
}