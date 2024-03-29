package pl.lotto.domain.numberreceiver.dto;

import lombok.Builder;
import org.hibernate.validator.constraints.UUID;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record TicketResponseDto(@UUID String ticketUUID,
                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                LocalDateTime drawDate,
                                @NotNull(message = "input numbers not null")
                                @NotEmpty(message = "input numbers not empty")
                                Set<Integer> inputNumbers,
                                String message) implements Serializable {
}
