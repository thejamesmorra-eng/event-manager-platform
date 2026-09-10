package dev.sorokin.eventmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Locations
                        .requestMatchers(HttpMethod.GET, "/locations", "/locations/**")
                        .hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/locations")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/locations/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/locations/**")
                        .hasRole("ADMIN")

                        // Users: публичные
                        .requestMatchers(HttpMethod.POST, "/users", "/users/auth")
                        .permitAll()

                        // Users: защищённые
                        .requestMatchers(HttpMethod.GET, "/users/**")
                        .authenticated()

                        // Всё остальное — требует аутентификации
                        .anyRequest().authenticated())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
