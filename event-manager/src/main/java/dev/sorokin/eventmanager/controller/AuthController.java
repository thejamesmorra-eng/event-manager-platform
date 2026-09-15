package dev.sorokin.eventmanager.controller;

import dev.sorokin.eventmanager.dto.request.UserCredentials;
import dev.sorokin.eventmanager.dto.response.JwtResponse;
import dev.sorokin.eventmanager.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<JwtResponse> authenticate(@Valid @RequestBody UserCredentials userCredentials) {
        log.debug("Authentication attempt for login: {}", userCredentials.login());
        return ResponseEntity.ok(authService.authenticateUser(userCredentials));
    }
}
