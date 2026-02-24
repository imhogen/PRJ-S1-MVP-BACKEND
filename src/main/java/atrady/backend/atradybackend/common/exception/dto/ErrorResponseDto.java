package atrady.backend.atradybackend.common.exception.dto;

import org.springframework.http.HttpStatus;

public record ErrorResponseDto(
        int statusCode,
        HttpStatus status,
        String errorMessage
) {}
