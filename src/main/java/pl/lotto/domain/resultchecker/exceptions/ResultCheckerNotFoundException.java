package pl.lotto.domain.resultchecker.exceptions;

public class ResultCheckerNotFoundException extends RuntimeException {
    public ResultCheckerNotFoundException(String ticketUUID) {
        super("Ticket UUID: " + ticketUUID + " not found");
    }
}
