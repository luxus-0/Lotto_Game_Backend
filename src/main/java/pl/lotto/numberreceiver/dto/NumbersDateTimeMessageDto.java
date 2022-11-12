package pl.lotto.numberreceiver.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record NumbersDateTimeMessageDto(Set<Integer> numbers, LocalDateTime dateTimeDraw, String dateDrawMessage) {
}
