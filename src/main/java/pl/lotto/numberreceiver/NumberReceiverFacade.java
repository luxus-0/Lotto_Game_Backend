package pl.lotto.numberreceiver;

import pl.lotto.numberreceiver.dto.NumbersResultMessageDto;

import java.util.Optional;
import java.util.Set;

import static pl.lotto.numberreceiver.NumberReceiverMessageProvider.*;
import static pl.lotto.numberreceiver.NumberValidator.checkMoreThanSixNumbers;
import static pl.lotto.numberreceiver.enums.ValidateMessageInfo.*;

public class NumberReceiverFacade {

    private final NumberValidator numberValidator;

    public NumberReceiverFacade(NumberValidator numberValidator) {
        this.numberValidator = numberValidator;
    }

    public NumbersResultMessageDto inputNumbers(Set<Integer> inputNumbers) {
        if (numberValidator.isLessThanSixNumbers(inputNumbers)) {
            NumbersResultMessageDto notCorrectResults = new NumbersResultMessageDto(inputNumbers, FAILED_MESSAGE);
            return Optional.of(notCorrectResults)
                    .orElse(new NumbersResultMessageDto(inputNumbers, SUCCESS_MESSAGE));
        }

        if (numberValidator.isEqualsSixNumbers(inputNumbers)) {
            NumbersResultMessageDto result = new NumbersResultMessageDto(inputNumbers, CORRECT_SIZE_NUMBERS.name());
            return new NumbersResultMessageDto(inputNumbers, result.message());
        }

        return new NumbersResultMessageDto(inputNumbers, INVALID_MESSAGE);
    }

    public NumbersResultMessageDto isMoreThanSixNumbers(Set<Integer> inputNumbers) {
        boolean greaterThanSixNumbers = inputNumbers.stream().anyMatch(numbers -> checkMoreThanSixNumbers(inputNumbers));
        if (greaterThanSixNumbers) {
            return Optional.of(new NumbersResultMessageDto(inputNumbers, NOT_CORRECT_SIZE_NUMBERS.name()))
                    .orElseGet(() -> new NumbersResultMessageDto(inputNumbers, CORRECT_SIZE_NUMBERS.name()));
        }
        return new NumbersResultMessageDto(Set.of(0), UNKNOWN_SIZE_NUMBERS.name());
    }
}
