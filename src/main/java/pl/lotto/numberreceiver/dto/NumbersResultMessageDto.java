package pl.lotto.numberreceiver.dto;

import java.util.List;
import java.util.Set;

public record NumbersResultMessageDto(Set<Integer> inputNumber, List<String> errorMessage) {}
