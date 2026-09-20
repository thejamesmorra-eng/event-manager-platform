package dev.sorokin.eventmanager.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sorokin.eventmanager.exception.ErrorMessageResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public CustomAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {
        log.warn("Access denied for {}: {}", request.getRequestURI(), accessDeniedException.getMessage());

        String detailedMessage = accessDeniedException.getMessage() != null
                ? accessDeniedException.getMessage()
                : "You don't have permission to access this resource";

        ErrorMessageResponse messageResponse = ErrorMessageResponse.of(
                "Forbidden",
                detailedMessage
        );

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().write(objectMapper.writeValueAsString(messageResponse));
    }
}
