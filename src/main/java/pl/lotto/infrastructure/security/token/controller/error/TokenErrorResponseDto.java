package pl.lotto.infrastructure.security.token.controller.error;

import org.springframework.http.HttpStatus;

public record TokenErrorResponseDto(String badCredentials, HttpStatus httpStatus) {
}
