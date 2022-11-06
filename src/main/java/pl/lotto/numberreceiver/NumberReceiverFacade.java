package pl.lotto.numberreceiver;

import pl.lotto.numberreceiver.dto.NumbersMessageDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static pl.lotto.numberreceiver.NumbersReceiverMessageProvider.EQUALS_SIX_NUMBERS;

public class NumberReceiverFacade {

    private final NumbersReceiverValidator numberValidator;
    private final NumberReceiverRepository numberReceiverRepository;
    private final LocalDateTime drawDateTime;
    private final Clock clock = Clock.systemUTC();

    public NumberReceiverFacade(NumbersReceiverValidator numberValidator, NumberReceiverRepository numberReceiverRepository, LocalDateTime drawDateTime) {
        this.numberValidator = numberValidator;
        this.numberReceiverRepository = numberReceiverRepository;
        this.drawDateTime = drawDateTime;
    }


    public NumbersMessageDto inputNumbers(Set<Integer> inputNumbers) {
       boolean validate = numberValidator.validate(inputNumbers);
       if(validate){
           DateTimeReceiver ticketDrawDate = new DateTimeReceiver(clock);
           LocalDateTime drawDate = ticketDrawDate.generateDrawDate(drawDateTime);
           NumberReceiverGenerator numberReceiverGenerator = new NumberReceiverGenerator();
           NumberReceiver numberReceiver = numberReceiverGenerator.generateUserTicket(inputNumbers, drawDate);
           numberReceiverRepository.save(numberReceiver);
           return new NumbersMessageDto(inputNumbers, EQUALS_SIX_NUMBERS);
       }
        return new NumbersMessageDto(inputNumbers, null);
    }
}
