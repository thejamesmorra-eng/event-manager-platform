package dev.sorokin.eventmanager.config;

import dev.sorokin.eventmanager.entity.UserEntity;
import dev.sorokin.eventmanager.model.UserRole;
import dev.sorokin.eventmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private static final String DEFAULT_ADMIN_LOGIN = "admin";
    private static final Integer DEFAULT_ADMIN_AGE = 18;
    private static final String DEFAULT_ADMIN_PASSWORD = "adminPassword";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByRole(UserRole.ADMIN)) {
            UserEntity adminEntity = new UserEntity(
                    DEFAULT_ADMIN_LOGIN,
                    DEFAULT_ADMIN_AGE,
                    passwordEncoder.encode(DEFAULT_ADMIN_PASSWORD),
                    UserRole.ADMIN
            );
            userRepository.save(adminEntity);
        }
    }
}
