package dev.sorokin.eventmanager.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            final String jwt = authorizationHeader.substring(BEARER_PREFIX.length());

            if (jwtService.isTokenValid(jwt)) {
                final String login = jwtService.getLoginFromToken(jwt);

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    try {
                        UserDetails userDetails = customUserDetailsService.loadUserByUsername(login);

                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        log.debug("Authenticated user: {}, roles: {}", login, userDetails.getAuthorities());

                    } catch (UsernameNotFoundException e) {
                        log.warn("User not found: {}", login);
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}