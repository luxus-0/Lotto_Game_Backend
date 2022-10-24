package pl.lotto.numberreceiver.dto;

import pl.lotto.numberreceiver.Ticket;

public record TicketMessageDto(Ticket ticket, String message) {
}
