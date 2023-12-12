package pl.lotto.domain.resultchecker.exceptions;

public class TicketNotSavedException extends RuntimeException {
    public TicketNotSavedException(){
        super("Ticket not saved to database");
    }
}
