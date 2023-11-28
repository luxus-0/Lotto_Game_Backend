package pl.lotto.domain.numberreceiver.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record TicketDto(String ticketId, Set<Integer> numbers,Set<Integer> hitNumbers, LocalDateTime drawDate, boolean isWinner, String message) {
}
