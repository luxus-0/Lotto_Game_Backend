package pl.lotto.numberreceiver;

import pl.lotto.numberreceiver.dto.NumbersDateTimeMessageDto;
import pl.lotto.numberreceiver.dto.NumbersMessageDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public class NumberReceiverFacade {

    private final NumbersReceiverValidator numberValidator;
    private final Clock clock;
    private final NumberReceiverRepository numberReceiverRepository;
    private final NumberReceiverGenerator numberReceiverGenerator;

    public NumberReceiverFacade(NumbersReceiverValidator numberValidator, NumberReceiverRepository numberReceiverRepository, NumberReceiverGenerator numberReceiverGenerator) {
        this.numberValidator = numberValidator;
        this.clock = Clock.systemDefaultZone();
        this.numberReceiverRepository = numberReceiverRepository;
        this.numberReceiverGenerator = numberReceiverGenerator;
    }


    public NumbersDateTimeMessageDto inputNumbers(Set<Integer> numbersFromUser) {
        boolean validate = numberValidator.validate(numbersFromUser);
        String numbersMessage = numberValidator.messages.stream().findAny().orElseThrow();
        if (validate) {
            UUID uuid = UUID.randomUUID();
            DateTimeReceiver dateTimeReceiver = new DateTimeReceiver(clock);
            LocalDateTime drawDateTime = dateTimeReceiver.readDateTimeDraw();
            NumberReceiver numberReceiver = numberReceiverGenerator.generateTicket(uuid, numbersFromUser, drawDateTime);
            numberReceiverRepository.save(numberReceiver);
            return new NumbersDateTimeMessageDto(numbersFromUser, numbersMessage, drawDateTime, true);
        }
        return new NumbersDateTimeMessageDto(numbersFromUser, numbersMessage, null, false);
    }
}