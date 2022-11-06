package pl.lotto.numberreceiver.dto;

import pl.lotto.numberreceiver.NumberReceiver;

public record NumbersDateMessageDto(NumberReceiver numberReceiver, String messageInfo) {
}
