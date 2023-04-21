package pl.lotto.domain.numberreceiver;

import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Document
@Builder
public record Ticket(String ticketId, Set<Integer> numbers, LocalDateTime drawDate) {
}
