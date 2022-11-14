package pl.lotto.numberreceiver;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record UserNumbers(UUID uuid, Set<Integer> numbersFromUser, LocalDateTime dateDraw) {
}
