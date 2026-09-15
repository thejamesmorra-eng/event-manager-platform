package dev.sorokin.eventmanager.controller;

import dev.sorokin.eventmanager.dto.request.UserRegistration;
import dev.sorokin.eventmanager.dto.response.UserResponse;
import dev.sorokin.eventmanager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRegistration userRegistration) {
        log.debug("Register user with login: {}", userRegistration.login());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.registerUser(userRegistration));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long userId) {
        log.debug("Get user by id: {}", userId);
        return ResponseEntity.ok(userService.getUserById(userId));
    }
}
