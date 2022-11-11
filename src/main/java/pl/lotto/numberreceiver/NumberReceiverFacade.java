package pl.lotto.numberreceiver;

import pl.lotto.numberreceiver.dto.NumbersMessageDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public class NumberReceiverFacade {

    private final NumbersReceiverValidator numberValidator;
    private final NumberReceiverRepository numberReceiverRepository;
    private final LocalDateTime drawDateTime;

    public NumberReceiverFacade(NumbersReceiverValidator numberValidator, NumberReceiverRepository numberReceiverRepository, LocalDateTime drawDateTime, NumberReceiverGenerator numberReceiverGenerator) {
        this.numberValidator = numberValidator;
        this.numberReceiverRepository = numberReceiverRepository;
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
            Set<Integer> savedUUIDNumbers = numberReceiverRepository.save(uuid, numbersFromUser);
            numberReceiverRepository.save(drawDate, savedUUIDNumbers);
                return new NumbersMessageDto(numbersFromUser, messageValidation);
        }
        return new NumbersMessageDto(numbersFromUser, messageValidation);
    }
}