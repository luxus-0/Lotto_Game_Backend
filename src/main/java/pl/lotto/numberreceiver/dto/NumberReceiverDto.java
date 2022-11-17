package pl.lotto.numberreceiver.dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record NumberReceiverDto(UUID uuid, Set<Integer> numbersFromUser, LocalDateTime dateTimeDraw) {
}
