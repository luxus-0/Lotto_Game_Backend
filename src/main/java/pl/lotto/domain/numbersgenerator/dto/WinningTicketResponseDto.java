package pl.lotto.domain.numbersgenerator.dto;

import lombok.Builder;
import org.hibernate.validator.constraints.UUID;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record WinningTicketResponseDto(@UUID String ticketUUID,
                                       @NotNull(message = "Winning inputNumbers not null")
                                       @NotEmpty(message = "Winning inputNumbers not empty")
                                       Set<Integer> winningNumbers,
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                       LocalDateTime drawDate,
                                       @NotBlank
                                       boolean isWinner,
                                       @NotBlank
                                       String message) {
}
