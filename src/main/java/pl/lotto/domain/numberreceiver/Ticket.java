package pl.lotto.domain.numberreceiver;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Document
@Builder
public record Ticket(@Id String ticketId, Set<Integer> numbersFromUser, LocalDateTime drawDate) {
}
