package pl.lotto.numberreceiver;

import pl.lotto.numberreceiver.dto.NumbersMessageDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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

    public NumbersMessageDto inputNumbers(Set<Integer> numbersFromUser) {
        boolean validate = numberValidator.validate(numbersFromUser);
        if (validate) {
            Clock clock = Clock.systemUTC();
            DateTimeReceiver ticketDrawDate = new DateTimeReceiver(clock);
            UUID uuid = UUID.randomUUID();
            LocalDateTime drawDate = ticketDrawDate.generateDrawDate(drawDateTime);
            Set<Integer> savedUUIDNumbers = numberReceiverRepository.save(uuid, numbersFromUser);
            Set<Integer> savedTicket = numberReceiverRepository.save(drawDate, savedUUIDNumbers);
            return new NumbersMessageDto(savedTicket, numberValidator.messages);
        }
        return throwNumbersNotFoundException();
    }

    private NumbersMessageDto throwNumbersNotFoundException() {
        return Optional.of(new NumbersMessageDto(null, numberValidator.messages)).orElseThrow();
    }
}