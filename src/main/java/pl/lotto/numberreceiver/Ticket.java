package pl.lotto.numberreceiver;

import java.time.LocalDateTime;
import java.util.Set;

public record Ticket(String hash, Set<Integer> numbersFromUser, LocalDateTime drawDate) {
}
