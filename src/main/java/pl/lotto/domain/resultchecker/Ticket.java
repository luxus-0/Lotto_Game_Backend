package pl.lotto.domain.resultchecker;

import lombok.Builder;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Document
public record Ticket(@UUID String ticketUUID,
                     @NotNull(message = "inputNumbers not null")
                     @NotEmpty(message = "input numbers not empty")
                     Set<Integer> numbers,
                     @NotBlank
                     Set<Integer> hitNumbers,
                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                     LocalDateTime drawDate,
                     @NotBlank
                     boolean isWinner,
                     @NotBlank
                     String message) {
}
