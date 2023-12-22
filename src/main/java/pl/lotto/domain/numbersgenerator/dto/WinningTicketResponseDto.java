package pl.lotto.domain.numbersgenerator.dto;

import lombok.Builder;
import org.hibernate.validator.constraints.UUID;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record WinningTicketResponseDto(@UUID String ticketUUID,
                                       @NotNull(message = "Winning numbers not null")
                                       @NotEmpty(message = "Winning numbers not empty")
                                       @Min(0)
                                       @Max(99)
                                       Set<Integer> winningNumbers,
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                       LocalDateTime drawDate,
                                       @NotBlank
                                       boolean isWinner,
                                       @NotBlank
                                       String message) {
}
