package pl.lotto.domain.numbersgenerator;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Document
public record WinningTicket(@Id String ticketId, Set<Integer> winningNumbers, LocalDateTime drawDate, String message) {
}