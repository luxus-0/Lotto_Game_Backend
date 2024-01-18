package pl.lotto.domain.numberreceiver;

import lombok.Getter;

@Getter
public enum TicketResultMessage {
    TICKET_WIN("WIN"),
    TICKET_LOSE("LOSE"),
    TICKET_NOT_FOUND("TICKET NOT FOUND");

    private final String message;
    TicketResultMessage(String message) {
        this.message = message;
    }
}
