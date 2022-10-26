package pl.lotto.numberreceiver.dto;

import pl.lotto.numberreceiver.ValidateMessage;

import java.util.Set;

public record ValidationDto(Set<Integer> numbersFromUser, ValidateMessage validateMessageInfo) {}
