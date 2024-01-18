package pl.lotto.domain.resultchecker.exceptions;

public class ResultCheckerNotFoundException extends Exception {
    public ResultCheckerNotFoundException(String ticketUUID) {
        super("Winning ticket UUID: " + ticketUUID + " not found");
    }
}
