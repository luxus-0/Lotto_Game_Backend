package pl.lotto.domain.numberreceiver;

import lombok.Getter;

@Getter
public enum TicketResultMessage {
    TICKET_WIN("WIN"),
    TICKET_LOSE("LOSE");

    private final String message;
    TicketResultMessage(String message) {
        this.message = message;
    }
}
