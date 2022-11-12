package pl.lotto.numberreceiver.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record NumbersDateTimeMessageDto(Set<Integer> numbers, String numbersMessage ,LocalDateTime dateTimeDraw, boolean isDrawDate) {
}
