package pl.lotto.infrastructure.resultannouncer.error;

import org.springframework.http.HttpStatus;

public record ResultAnnouncerErrorResponse(String message, HttpStatus status) {
}
