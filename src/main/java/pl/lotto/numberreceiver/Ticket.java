package pl.lotto.numberreceiver;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public
record Ticket(String hash, Set<Integer> numbersUser, LocalDateTime drawDate) {
}
