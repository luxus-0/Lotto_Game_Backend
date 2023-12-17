package pl.lotto.domain.numberreceiver;

import lombok.Getter;
import pl.lotto.domain.numberreceiver.exceptions.InputNumbersNotFoundException;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static pl.lotto.domain.numberreceiver.NumberReceiverValidationResult.*;

@Getter
class NumbersReceiverValidator {

    private static final int QUANTITY_NUMBERS_FROM_USER = 6;
    private static final int MIN_NUMBER_FROM_USER = 1;
    private static final int MAX_NUMBER_FROM_USER = 99;

    private static List<String> errors;

    boolean validate(Set<Integer> inputNumbers) {
        errors = new LinkedList<>();
        if(inputNumbers == null || inputNumbers.isEmpty()){
            throw new InputNumbersNotFoundException("InputNumbers not found");
        }
        if (isEqualsSixNumberFrom1To99(inputNumbers)) {
            errors.add(EQUALS_SIX_NUMBERS.getInfo());
            return true;
        } else if (isLessThanSixNumbers(inputNumbers)) {
            errors.add(LESS_THAN_SIX_NUMBERS.getInfo());
            return true;
        } else if (isMoreThanSixNumbers(inputNumbers)) {
                errors.add(MORE_THAN_SIX_NUMBERS.getInfo());
                return true;
        } else if (isOutOfRange(inputNumbers)) {
                errors.add(OUT_OF_RANGE_NUMBERS.getInfo());
                return true;
        }
       return false;
    }

    boolean isLessThanSixNumbers(Set<Integer> inputNumbers) {
        return inputNumbers.size() > MIN_NUMBER_FROM_USER && inputNumbers.size() < QUANTITY_NUMBERS_FROM_USER;
    }

    boolean isMoreThanSixNumbers(Set<Integer> inputNumbers) {
        return inputNumbers.size() > QUANTITY_NUMBERS_FROM_USER;
    }

    boolean isEqualsSixNumberFrom1To99(Set<Integer> inputNumbers) {
        return inputNumbers.size() == QUANTITY_NUMBERS_FROM_USER;
    }

    boolean isOutOfRange(Set<Integer> inputNumbers) {
        return inputNumbers.stream()
                .anyMatch(number -> number < MIN_NUMBER_FROM_USER || number > MAX_NUMBER_FROM_USER);
    }

    String getMessage(){
        return errors.stream()
                .map(String::valueOf)
                .findAny()
                .orElse("Validation error");
    }
}
