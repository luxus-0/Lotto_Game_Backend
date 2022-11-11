package pl.lotto.numberreceiver;

import pl.lotto.numberreceiver.dto.NumbersMessageDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public class NumberReceiverFacade {

    private final NumbersReceiverValidator numberValidator;
    private final NumberReceiverRepository numberReceiverRepository;
    private final NumberReceiverGenerator numberReceiverGenerator;
    private final LocalDateTime drawDateTime;

    public NumberReceiverFacade(NumbersReceiverValidator numberValidator, NumberReceiverRepository numberReceiverRepository,LocalDateTime drawDateTime, NumberReceiverGenerator numberReceiverGenerator) {
        this.numberValidator = numberValidator;
        this.numberReceiverRepository = numberReceiverRepository;
        this.numberReceiverGenerator = numberReceiverGenerator;
        this.drawDateTime = drawDateTime;
    }


    public NumbersMessageDto inputNumbers(Set<Integer> numbersFromUser) {
        boolean validate = numberValidator.validate(numbersFromUser);
        String messageValidation = numberValidator.messages.stream().findAny().orElseThrow();
        if (validate) {
            Clock clock = Clock.systemUTC();
            DateTimeReceiver ticketDrawDate = new DateTimeReceiver(clock);
            UUID uuid = UUID.randomUUID();
            LocalDateTime drawDate = ticketDrawDate.generateDrawDate(drawDateTime);
            NumberReceiver numberReceiver = numberReceiverGenerator.generateTicket(uuid,numbersFromUser, drawDate);
            NumberReceiver savedReceiver = numberReceiverRepository.save(numberReceiver);
            return new NumbersMessageDto(savedReceiver.numbersFromUser(), messageValidation);
        }
        return new NumbersMessageDto(numbersFromUser, messageValidation);
    }
}