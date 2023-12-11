package pl.lotto.domain.resultannouncer.exceptions;

public class TicketUUIDNotFoundException extends Exception {
    public TicketUUIDNotFoundException() {
        super("Ticket UUID not found");
    }
}
