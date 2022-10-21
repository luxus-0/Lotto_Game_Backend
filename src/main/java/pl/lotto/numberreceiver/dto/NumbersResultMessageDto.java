package pl.lotto.numberreceiver.dto;

import java.util.Set;

public record NumbersResultMessageDto(Set<Integer> inputNumber, String message) {}
