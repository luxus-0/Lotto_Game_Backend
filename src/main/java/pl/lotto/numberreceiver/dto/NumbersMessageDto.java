package pl.lotto.numberreceiver.dto;

import java.util.List;
import java.util.Set;

public record NumbersMessageDto(Set<Integer> inputNumber, List<String> messages) {
}
