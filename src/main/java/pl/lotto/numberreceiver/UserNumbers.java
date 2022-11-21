package pl.lotto.numberreceiver;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Builder
record UserNumbers(UUID uuid, Set<Integer> numbersFromUser, LocalDateTime dateTimeDraw) {
}
