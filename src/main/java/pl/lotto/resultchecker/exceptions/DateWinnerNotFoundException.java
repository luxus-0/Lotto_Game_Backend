package pl.lotto.resultchecker.exceptions;

import java.time.LocalDateTime;

public class DateWinnerNotFoundException extends RuntimeException {
    public DateWinnerNotFoundException(LocalDateTime dateTime) {
        System.err.println("In Date: " + dateTime + " numbers winner not found");
    }
}
