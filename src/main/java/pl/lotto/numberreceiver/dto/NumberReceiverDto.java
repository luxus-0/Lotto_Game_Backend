package pl.lotto.numberreceiver.dto;

import java.util.Set;
public record NumberReceiverDto(Set<Integer> numbers, String message){}
