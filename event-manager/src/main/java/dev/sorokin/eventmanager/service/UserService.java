package dev.sorokin.eventmanager.service;

import dev.sorokin.eventmanager.dto.request.UserRegistration;
import dev.sorokin.eventmanager.dto.response.UserResponse;
import dev.sorokin.eventmanager.entity.UserEntity;
import dev.sorokin.eventmanager.exception.LoginAlreadyExistsException;
import dev.sorokin.eventmanager.mapper.UserMapper;
import dev.sorokin.eventmanager.model.UserRole;
import dev.sorokin.eventmanager.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    public UserResponse registerUser(UserRegistration userRegistration) {
        if (userRepository.existsByLogin(userRegistration.login())) {
            throw new LoginAlreadyExistsException("The login: %s already exists".formatted(userRegistration.login()));
        }
        String passwordHash = passwordEncoder.encode(userRegistration.password());

        UserEntity userEntity = new UserEntity(
                userRegistration.login(),
                userRegistration.age(),
                passwordHash,
                UserRole.USER
        );
        return userMapper.toResponse(userRepository.save(userEntity));
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User entity with id: %s not found".formatted(id)));
        return userMapper.toResponse(userEntity);
    }
}
