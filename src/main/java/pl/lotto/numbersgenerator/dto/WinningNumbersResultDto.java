package pl.lotto.numbersgenerator.dto;

import pl.lotto.numberreceiver.Ticket;

public record WinningNumbersResultDto(Ticket ticket, String messageInfo) {
}
