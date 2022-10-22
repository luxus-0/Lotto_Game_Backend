package pl.lotto.numberreceiver;

import pl.lotto.numberreceiver.dto.NumbersResultMessageDto;
import pl.lotto.numberreceiver.enums.ValidateMessage;
import pl.lotto.numberreceiver.exception.DuplicateNumbersNotFoundException;
import pl.lotto.numberreceiver.exception.RangeNumbersException;
import pl.lotto.numberreceiver.exception.NumbersNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static pl.lotto.numberreceiver.NumberReceiverMessageProvider.*;
import static pl.lotto.numberreceiver.NumbersDuplicationInfo.duplicateNumbersInfo;
import static pl.lotto.numberreceiver.enums.ValidateMessage.*;

public class NumberReceiverFacade {

    private final NumberReceiverValidator numberValidator;

    public NumberReceiverFacade(NumberReceiverValidator numberValidator) {
        this.numberValidator = numberValidator;
    }

    public NumbersResultMessageDto isLessThanSixNumbers(Set<Integer> inputNumbers) {
        if (numberValidator.checkLessThanSixNumbers(inputNumbers)) {
            NumbersResultMessageDto notCorrectResults = new NumbersResultMessageDto(inputNumbers, FAILED_MESSAGE);
            return Optional.of(notCorrectResults).orElse(new NumbersResultMessageDto(inputNumbers, SUCCESS_MESSAGE));
        }
        throw new NumbersNotFoundException();
    }

    public NumbersResultMessageDto isEqualsSixNumbers(Set<Integer> inputNumbers) {
        if (numberValidator.checkEqualsSixNumbers(inputNumbers)) {
            NumbersResultMessageDto result = new NumbersResultMessageDto(inputNumbers, CORRECT_SIZE_NUMBERS.name());
            return new NumbersResultMessageDto(inputNumbers, result.message());
        }
        return new NumbersResultMessageDto(inputNumbers, INVALID_MESSAGE);
    }

    public NumbersResultMessageDto isMoreThanSixNumbers(Set<Integer> inputNumbers) {
        if (numberValidator.checkMoreThanSixNumbers(inputNumbers)) {
            return Optional.of(new NumbersResultMessageDto(inputNumbers, NOT_CORRECT_SIZE_NUMBERS.name()))
                    .orElseGet(() -> new NumbersResultMessageDto(inputNumbers, CORRECT_SIZE_NUMBERS.name()));
        }
        return new NumbersResultMessageDto(null, UNKNOWN_SIZE_NUMBERS.name());

    }

    public ValidateMessage isDuplicateNumbers(List<Integer> numbersCheck) {
        NumbersDuplicationChecker numbersFinder = new NumbersDuplicationChecker();
        if (numbersFinder.checkIdenticalNumbers(numbersCheck)) {
            return duplicateNumbersInfo();
        }
        throw new DuplicateNumbersNotFoundException();
    }

    public ValidateMessage isNumbersNotInRange(Set<Integer> inputNumbers) {
        NumberReceiverRangeChecker numbers = new NumberReceiverRangeChecker();
        if (numbers.checkNumbersInRange(inputNumbers)) {
            return IN_RANGE_NUMBERS;
        }
        throw new RangeNumbersException();
    }
}
