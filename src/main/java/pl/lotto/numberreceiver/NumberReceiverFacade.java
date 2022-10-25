package pl.lotto.numberreceiver;

import pl.lotto.numberreceiver.dto.NumbersResultMessageDto;
import pl.lotto.numberreceiver.dto.TicketMessageDto;

import java.time.LocalDateTime;
import java.util.Set;

import static pl.lotto.numberreceiver.TicketMessageProvider.ticket_failed;
import static pl.lotto.numberreceiver.TicketMessageProvider.ticket_ok;

public class NumberReceiverFacade {

    private final NumbersValidator numberValidator;
    private final TicketRepository ticketRepository;
    private final TicketGenerator ticketGenerator;

    public NumberReceiverFacade(NumbersValidator numberValidator, TicketRepository ticketRepository, TicketGenerator ticketGenerator) {
        this.numberValidator = numberValidator;
        this.ticketRepository = ticketRepository;
        this.ticketGenerator = ticketGenerator;
    }

    public NumbersResultMessageDto inputNumbers(Set<Integer> inputNumbers) {
       boolean validate = numberValidator.validate(inputNumbers);
       if(validate){
           LocalDateTime date = ticketGenerator.generateDate();
           String hash = ticketGenerator.generateHash();
           Ticket ticketCreated = ticketGenerator.generateTicket(inputNumbers, hash, date);
           Ticket ticket = ticketRepository.save(ticketCreated);
           TicketMessageDto ticketMessage = new TicketMessageDto(ticket, ticket_ok());
           return new NumbersResultMessageDto(inputNumbers, ticketMessage.message());
       }
        return new NumbersResultMessageDto(inputNumbers, ticket_failed());
    }
}
