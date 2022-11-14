package pl.lotto.numberreceiver;

import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record NumberReceiverDto(UUID uuid, Set<Integer> numbersFromUser, java.time.LocalDateTime dateTimeDraw) {
}
