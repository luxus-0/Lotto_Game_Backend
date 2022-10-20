package pl.lotto.numberreceiver;

import pl.lotto.numberreceiver.dto.NumberReceiverDto;

import java.util.Collection;
import java.util.Set;

import static pl.lotto.numberreceiver.NumberReceiverMessageProvider.FAILED_MESSAGE;
import static pl.lotto.numberreceiver.NumberReceiverMessageProvider.SUCCESS_MESSAGE;

public class NumberReceiverFacade {
    private final NumberValidator numberValidator;

    public NumberReceiverFacade(NumberValidator numberValidator) {
        this.numberValidator = numberValidator;
    }

    public NumberReceiverDto inputNumbers(Set<Integer> numbersFromUser) {
        if (numberValidator.isLessThanSixNumbers(numbersFromUser)) {
            return new NumberReceiverDto(numbersFromUser, SUCCESS_MESSAGE);
        }
        return new NumberReceiverDto(numbersFromUser, FAILED_MESSAGE);
    }

}
