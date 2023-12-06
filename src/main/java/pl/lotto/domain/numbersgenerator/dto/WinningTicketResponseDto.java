package pl.lotto.domain.numbersgenerator.dto;

import lombok.Builder;
import org.hibernate.validator.constraints.UUID;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record WinningTicketResponseDto(@UUID String ticketId,
                                       Set<Integer> winningNumbers,
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                       LocalDateTime drawDate,
                                       String message) {
}
