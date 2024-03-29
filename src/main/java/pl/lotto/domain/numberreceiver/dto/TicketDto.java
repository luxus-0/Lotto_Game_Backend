package pl.lotto.domain.numberreceiver.dto;

import lombok.Builder;
import org.hibernate.validator.constraints.UUID;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record TicketDto(@UUID
                        String ticketUUID,
                        @NotNull(message = "Input numbers not null")
                        @NotEmpty(message = "Input numbers not empty")
                        Set<Integer> inputNumbers,
                        Set<Integer> hitNumbers,
                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                        LocalDateTime drawDate,
                        @NotBlank
                        boolean isWinner,
                        @NotBlank
                        String message) {
}
