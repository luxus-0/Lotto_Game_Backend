package pl.lotto.domain.numberreceiver.dto;

import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record TicketDto(String ticketId,
                        Set<Integer> numbers,
                        Set<Integer> hitNumbers,
                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                        LocalDateTime drawDate,
                        boolean isWinner,
                        String message) {
}
