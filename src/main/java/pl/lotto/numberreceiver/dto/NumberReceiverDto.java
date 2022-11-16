package pl.lotto.numberreceiver.dto;

import lombok.Builder;

import java.util.Set;
import java.util.UUID;

public record NumberReceiverDto(UUID uuid, Set<Integer> numbersFromUser, java.time.LocalDateTime dateTimeDraw) {
}
