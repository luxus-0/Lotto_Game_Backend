package pl.lotto.infrastructure.security.token.controller.error.dto;

import org.springframework.http.HttpStatus;

public record TokenErrorResponseDto(String badCredentials, HttpStatus httpStatus) {
}
