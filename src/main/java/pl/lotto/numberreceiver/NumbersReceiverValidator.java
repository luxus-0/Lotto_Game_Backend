package pl.lotto.numberreceiver;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static pl.lotto.numberreceiver.NumbersReceiverMessageProvider.*;

@Service
class NumbersReceiverValidator {

    List<String> messages = new LinkedList<>();

    boolean validate(Set<Integer> inputNumbers) {
        if (isLessThanSixNumbers(inputNumbers)) {
            messages.add(LESS_THAN_SIX_NUMBERS);
        } else if (isMoreThanSixNumbers(inputNumbers)) {
            messages.add(MORE_THAN_SIX_NUMBERS);
        } else if (isEmptyNumbers(inputNumbers)) {
            messages.add(NUMBERS_IS_EMPTY);
        } else if (isNotInRangeNumbers(inputNumbers)) {
            messages.add(NOT_IN_RANGE_NUMBERS);
        } else {
            messages.add(EQUALS_SIX_NUMBERS);
            return isEqualsSixNumbers(inputNumbers);
        }
        return false;
    }


    boolean isEmptyNumbers(Set<Integer> inputNumbers) {
        return inputNumbers.isEmpty();
    }

    boolean isLessThanSixNumbers(Collection<Integer> inputNumbers) {
        return inputNumbers.size() < SIZE_MAX;
    }

    boolean isEqualsSixNumbers(Collection<Integer> inputNumbers) {
        return inputNumbers.size() == SIZE_MAX && isPositiveNumber(inputNumbers);
    }

    private boolean isPositiveNumber(Collection<Integer> inputNumbers) {
        return inputNumbers.stream().allMatch(number -> number > 0);
    }

    boolean isNotInRangeNumbers(Collection<Integer> inputNumbers) {
        return inputNumbers.stream().anyMatch(number -> number > RANGE_TO_NUMBER || number < RANGE_FROM_NUMBER);
    }

    boolean isMoreThanSixNumbers(Collection<Integer> inputNumbers) {
        return inputNumbers.size() > SIZE_MAX;
    }
}
