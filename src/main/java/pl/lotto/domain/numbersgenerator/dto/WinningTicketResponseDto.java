package pl.lotto.domain.numbersgenerator.dto;

import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record WinningTicketResponseDto(String ticketId,
                                       Set<Integer> winningNumbers,
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                       LocalDateTime drawDate,
                                       String message) {
}
