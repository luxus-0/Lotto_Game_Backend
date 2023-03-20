package pl.lotto.numberreceiver;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
class NumbersReceiverValidator {

    private static final int QUANTITY_NUMBERS_FROM_USER = 6;
    private static final int MIN_NUMBER_FROM_USER = 1;
    private static final int MAX_NUMBER_FROM_USER = 99;

    List<ValidationResult> errors = new LinkedList<>();

    boolean validate(Set<Integer> inputNumbers) {
        if (isLessThanSixNumbers(inputNumbers)) {
            errors.add(ValidationResult.LESS_THAN_SIX_NUMBERS);
        } else if (isMoreThanSixNumbers(inputNumbers)) {
            errors.add(ValidationResult.MORE_THAN_SIX_NUMBERS);
        } else if (isEmptyNumbers(inputNumbers)) {
            errors.add(ValidationResult.EMPTY_NUMBERS);
        }
        else if(isNumberNotInRange(inputNumbers)){
            errors.add(ValidationResult.OUT_OF_RANGE_NUMBERS);
        }
        else {
            errors.add(ValidationResult.EQUALS_SIX_NUMBERS);
            return isEqualsSixNumberFrom1To99(inputNumbers);
        }
        return false;
    }


    boolean isEmptyNumbers(Set<Integer> inputNumbers) {
        return inputNumbers.size() == 0;
    }

    boolean isLessThanSixNumbers(Collection<Integer> inputNumbers) {
        return inputNumbers.size() >= MIN_NUMBER_FROM_USER && inputNumbers.size() < QUANTITY_NUMBERS_FROM_USER;
    }

    boolean isMoreThanSixNumbers(Collection<Integer> inputNumbers) {
        return inputNumbers.size() > QUANTITY_NUMBERS_FROM_USER;
    }

    boolean isEqualsSixNumberFrom1To99(Collection<Integer> inputNumbers) {
        if(areAllNumbersInRange(inputNumbers)){
            return true;
        }
        return false;
    }

    boolean areAllNumbersInRange(Collection<Integer> inputNumbers) {
        return inputNumbers.stream()
                .filter(number -> number >= MIN_NUMBER_FROM_USER)
                .filter(number -> number <= MAX_NUMBER_FROM_USER)
                .count() == QUANTITY_NUMBERS_FROM_USER;
    }

    boolean isNumberNotInRange(Collection<Integer> inputNumbers) {
        return inputNumbers.stream()
                .anyMatch(number -> number < MIN_NUMBER_FROM_USER || number > MAX_NUMBER_FROM_USER);

    }
}
