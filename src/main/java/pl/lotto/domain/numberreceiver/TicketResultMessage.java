package pl.lotto.domain.numberreceiver;

import lombok.Getter;

@Getter
public enum TicketResultMessage {
    WIN("WIN"),
    LOSE("LOSE");

    private final String message;
    TicketResultMessage(String message) {
        this.message = message;
    }
}
