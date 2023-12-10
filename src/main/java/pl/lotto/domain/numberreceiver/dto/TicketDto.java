package pl.lotto.domain.numberreceiver.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import org.hibernate.validator.constraints.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import pl.lotto.domain.numberreceiver.TicketResultMessage;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record TicketDto(@UUID
                        String ticketUUID,
                        @NotNull(message = "input numbers not null")
                        @NotEmpty(message = "input numbers not empty")
                        Set<Integer> numbers,
                        Set<Integer> hitNumbers,
                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                        LocalDateTime drawDate,
                        @NotBlank
                        boolean isWinner,
                        @Enumerated(value = EnumType.STRING)
                        TicketResultMessage ticketResultMessage) {
}
