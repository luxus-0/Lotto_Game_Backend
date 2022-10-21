package pl.lotto.numberreceiver.dto;

import pl.lotto.numberreceiver.enums.ValidateMessageInfo;

import java.util.Set;

public record ValidationDto(Set<Integer> numbersFromUser, ValidateMessageInfo validateMessageInfo) {}
