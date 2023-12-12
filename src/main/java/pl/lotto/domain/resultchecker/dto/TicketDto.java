package pl.lotto.domain.resultchecker.dto;

import lombok.Builder;
import org.hibernate.validator.constraints.UUID;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record TicketDto(@UUID @NotNull String ticketUUID,
                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                        LocalDateTime drawDate,
                        @NotNull(message = "Input numbers not null")
                        @NotEmpty(message = "Input numbers not empty")
                        Set<Integer> inputNumbers,
                        @NotBlank
                        @Min(0)
                        Set<Integer> hitNumbers,
                        @NotNull(message = "Winning numbers not null")
                        @NotEmpty(message = "Winning numbers not empty")
                        Set<Integer> winningNumbers,
                        boolean isWinner,
                        String message
) {
}
