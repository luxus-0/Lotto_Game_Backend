package pl.lotto.numberreceiver;

import pl.lotto.numberreceiver.dto.NumbersResultMessageDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public class NumberReceiverFacade {

    private final NumbersValidator numberValidator;
    private final TicketRepository ticketRepository;
    private final TicketGenerator ticketGenerator;
    private final LocalDateTime drawDateTime;

    public NumberReceiverFacade(NumbersValidator numberValidator, TicketRepository ticketRepository, TicketGenerator ticketGenerator, LocalDateTime drawDateTime) {
        this.numberValidator = numberValidator;
        this.ticketRepository = ticketRepository;
        this.ticketGenerator = ticketGenerator;
        this.drawDateTime = drawDateTime;
    }

    public NumbersResultMessageDto inputNumbers(Set<Integer> inputNumbers) {
       boolean validate = numberValidator.validate(inputNumbers);
       if(validate){
           TicketCurrentDateTime currentDateTime = new TicketCurrentDateTime(Clock.systemUTC());
           TicketDrawDate ticketDrawDate = new TicketDrawDate(currentDateTime, clock);
           LocalDateTime drawDate = ticketDrawDate.generateDrawDate(drawDateTime);
           Ticket ticketCreated = ticketGenerator.generateTicket(inputNumbers, drawDate);
           ticketRepository.save(ticketCreated);
           return new NumbersResultMessageDto(inputNumbers, numberValidator.messagesValidation);
       }
        NumbersResultMessageDto messageResult = new NumbersResultMessageDto(inputNumbers, numberValidator.messagesValidation);
        return Optional.of(messageResult).orElseThrow();
    }
}
