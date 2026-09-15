package dev.sorokin.eventmanager.config;

import dev.sorokin.eventmanager.entity.UserEntity;
import dev.sorokin.eventmanager.model.UserRole;
import dev.sorokin.eventmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.default.login}")
    private String defaultAdminLogin;

    @Value("${admin.default.age}")
    private int defaultAdminAge;

    @Value("${admin.default.password}")
    private String defaultAdminPassword;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByRole(UserRole.ADMIN)) {
            UserEntity adminEntity = new UserEntity(
                    defaultAdminLogin,
                    defaultAdminAge,
                    passwordEncoder.encode(defaultAdminPassword),
                    UserRole.ADMIN
            );
            userRepository.save(adminEntity);
        }
    }
}
