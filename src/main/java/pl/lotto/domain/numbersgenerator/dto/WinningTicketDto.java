package pl.lotto.domain.numbersgenerator.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record WinningTicketDto(String ticketId, Set<Integer> winningNumbers, LocalDateTime drawDate, String message) {
}
