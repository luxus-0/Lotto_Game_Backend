package pl.lotto.domain.numberreceiver.exceptions;

public class WinningTicketNotFoundException extends Exception {
    public WinningTicketNotFoundException(String message) {
        super(message);
    }
}