package pl.lotto.domain.numberreceiver.exceptions;

public class WinningTicketNotFoundException extends RuntimeException {
    public WinningTicketNotFoundException(String message) {
        super(message);
    }

}
