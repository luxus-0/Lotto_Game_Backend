package pl.lotto.domain.numberreceiver.exceptions;

public class WinningTicketNotFoundException extends RuntimeException {
    public WinningTicketNotFoundException() {
        super("Winning ticket not found");
    }

}
