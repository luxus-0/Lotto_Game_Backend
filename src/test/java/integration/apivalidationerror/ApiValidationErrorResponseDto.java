package integration.apivalidationerror;

import org.springframework.http.HttpStatus;

public record ApiValidationErrorResponseDto(String message, HttpStatus status) {
}
