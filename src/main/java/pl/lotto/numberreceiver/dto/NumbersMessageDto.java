package pl.lotto.numberreceiver.dto;

import java.util.Set;

public record NumbersMessageDto(Set<Integer> inputNumber, java.util.List<String> messages) {
}
