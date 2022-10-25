package pl.lotto.numberreceiver;

public class NumberReceiverFacadeConfiguration {
    NumberReceiverFacade createModuleForTests(TicketRepository ticketRepository){
        NumbersValidator numbersValidator = new NumbersValidator();
        TicketGenerator ticketGenerator = new TicketGenerator();
        return new NumberReceiverFacade(numbersValidator, ticketRepository, ticketGenerator);
    }
}
