package pl.lotto.numberreceiver;

import pl.lotto.numberreceiver.dto.NumbersResultMessageDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class NumberReceiverFacade {

    private final static String NUMBERS_NOT_FOUND = "Numbers not found";
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
           Ticket ticketCreated = ticketGenerator.generateTicket(inputNumbers);
           ticketRepository.save(ticketCreated);
           return new NumbersResultMessageDto(inputNumbers, numberValidator.messageValidations);
       }
       return new NumbersResultMessageDto(inputNumbers, numbersNotFound());
    }

    private List<String> numbersNotFound() {
        return List.of(NUMBERS_NOT_FOUND);
    }
}
