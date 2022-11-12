package pl.lotto.numberreceiver;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Builder
public record NumberReceiver(UUID uuid, Set<Integer> numbersFromUser, LocalDateTime drawDate) {
}
