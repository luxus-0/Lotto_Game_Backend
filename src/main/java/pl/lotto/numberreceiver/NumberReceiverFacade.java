package pl.lotto.numberreceiver;

import pl.lotto.numberreceiver.dto.DateTimeMessageDto;
import pl.lotto.numberreceiver.dto.NumbersMessageDto;

import java.util.Set;
import java.util.UUID;

public class NumberReceiverFacade {

    private final NumbersReceiverValidator numberValidator;
    private final DateTimeReceiverValidator dateTimeValidator;
    private final NumberReceiverRepository numberReceiverRepository;
    private final NumberReceiverGenerator numberReceiverGenerator;

    public NumberReceiverFacade(NumbersReceiverValidator numberValidator, DateTimeReceiverValidator dateTimeValidator, NumberReceiverRepository numberReceiverRepository, NumberReceiverGenerator numberReceiverGenerator) {
        this.numberValidator = numberValidator;
        this.dateTimeValidator = dateTimeValidator;
        this.numberReceiverRepository = numberReceiverRepository;
        this.numberReceiverGenerator = numberReceiverGenerator;
    }


    public NumbersMessageDto inputNumbers(Set<Integer> numbersFromUser) {
        boolean validateNumbers = numberValidator.validate(numbersFromUser);
        boolean validateDate = dateTimeValidator.isCorrectDateTimeDraw();
        String messageValidation = numberValidator.messages.stream().findAny().orElseThrow();
        if (validateNumbers && validateDate) {
            UUID uuid = UUID.randomUUID();
            NumberReceiver numberReceiver = numberReceiverGenerator.generateTicket(uuid, numbersFromUser, validateDate);
            NumberReceiver savedReceiver = numberReceiverRepository.save(numberReceiver);
            return new NumbersMessageDto(savedReceiver.numbersFromUser(), messageValidation);
        }
        return new NumbersMessageDto(numbersFromUser, messageValidation);
    }

    public DateTimeMessageDto generateDateMessage(){
        if(dateTimeValidator.isCorrectDateTimeDraw()){
            return new DateTimeMessageDto("success");
        }
        return new DateTimeMessageDto("failed");
    }
}