package pl.lotto.domain.resultchecker.exceptions;

public class TicketNotSavedException extends Exception {
    public TicketNotSavedException(){
        super("Ticket not saved to database");
    }
}
