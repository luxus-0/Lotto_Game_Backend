package pl.lotto.numberreceiver.dto;

import pl.lotto.numberreceiver.Ticket;

public record TicketResultMessageDto(Ticket ticket, String message) {
}
