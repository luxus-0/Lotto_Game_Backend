package pl.lotto.domain.numberreceiver;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Document
public
record Ticket(String hash, Set<Integer> numbersFromUser, LocalDateTime drawDate) {
}
