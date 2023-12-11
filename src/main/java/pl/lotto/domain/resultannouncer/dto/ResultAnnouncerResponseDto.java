package pl.lotto.domain.resultannouncer.dto;

import lombok.Builder;
import org.hibernate.validator.constraints.UUID;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record ResultAnnouncerResponseDto(@UUID
                                         String ticketUUID,
                                         @NotNull(message = "inputNumbers not null")
                                         @NotEmpty(message = "inputNumbers not empty")
                                         Set<Integer> inputNumbers,
                                         @NotBlank
                                         Set<Integer> hitNumbers,
                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                         LocalDateTime drawDate,
                                         boolean isWinner,
                                         @NotNull
                                         @NotEmpty
                                         String message) {
}
