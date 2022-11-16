package pl.lotto.numberreceiver;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

record UserNumbers(UUID uuid, Set<Integer> numbersFromUser, LocalDateTime dateTimeDraw) {
}
