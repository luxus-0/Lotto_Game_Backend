package pl.lotto.numberreceiver;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record NumberReceiver(String uuid, Set<Integer> numbersFromUser, LocalDateTime drawDate) {
}
