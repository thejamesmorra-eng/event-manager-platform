package dev.sorokin.eventmanager.controller;

import dev.sorokin.eventmanager.dto.request.UserRegisterRequest;
import dev.sorokin.eventmanager.dto.response.UserResponse;
import dev.sorokin.eventmanager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody UserRegisterRequest request) {
        return null;
    }

    @PostMapping("/auth")
    public ResponseEntity<Void> getAuth() {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@Valid @PathVariable Long id) {
        return null;
    }
}
