package pl.lotto.numberreceiver;

import pl.lotto.numberreceiver.dto.NumbersMessageDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Set;

public class NumberReceiverFacade {

    private final NumbersReceiverValidator numberValidator;
    private final NumberReceiverRepository numberReceiverRepository;
    private final LocalDateTime drawDateTime;
    private final NumberReceiverGenerator numberReceiverGenerator;

    public NumberReceiverFacade(NumbersReceiverValidator numberValidator, NumberReceiverRepository numberReceiverRepository, LocalDateTime drawDateTime, NumberReceiverGenerator numberReceiverGenerator) {
        this.numberValidator = numberValidator;
        this.numberReceiverRepository = numberReceiverRepository;
        this.drawDateTime = drawDateTime;
        this.numberReceiverGenerator = numberReceiverGenerator;
    }

    public NumbersMessageDto inputNumbers(Set<Integer> inputNumbers) {
        boolean validate = numberValidator.validate(inputNumbers);
        if (validate) {
            Clock clock = Clock.systemUTC();
            DateTimeReceiver ticketDrawDate = new DateTimeReceiver(clock);
            LocalDateTime drawDate = ticketDrawDate.generateDrawDate(drawDateTime);
            NumberReceiver numberReceiver = numberReceiverGenerator.generateUserTicket(inputNumbers, drawDate);
            numberReceiverRepository.save(numberReceiver);
        }
        return new NumbersMessageDto(inputNumbers, numberValidator.messages);
    }
}