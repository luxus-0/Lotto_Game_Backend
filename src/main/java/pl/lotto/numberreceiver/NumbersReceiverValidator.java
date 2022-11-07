package pl.lotto.numberreceiver;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static pl.lotto.numberreceiver.NumbersReceiverMessageProvider.*;

public class NumbersReceiverValidator {

    List<String> messages = new LinkedList<>();

    public boolean validate(Set<Integer> inputNumbers) {
        if (isEqualsSixNumbers(inputNumbers)) {
            messages.add(EQUALS_SIX_NUMBERS);
        } else if (isLessThanSixNumbers(inputNumbers)) {
            messages.add(LESS_THAN_SIX_NUMBERS);
        } else if (isMoreThanSixNumbers(inputNumbers)) {
            messages.add(MORE_THAN_SIX_NUMBERS);
        } else if (!isInRangeNumbers()) {
            messages.add(NOT_IN_RANGE_NUMBERS);
        } else {
            messages.add(NUMBERS_IS_EMPTY);
        }
        return false;
    }

    public boolean isLessThanSixNumbers(Collection<Integer> inputNumbers) {
        return inputNumbers.size() < SIZE_MAX;
    }

    public boolean isEqualsSixNumbers(Collection<Integer> inputNumbers) {
        return inputNumbers.size() == SIZE_MAX;
    }

    public boolean isInRangeNumbers() {
        return IntStream.rangeClosed(RANGE_FROM_NUMBER, RANGE_TO_NUMBER)
                .findAny()
                .isPresent();
    }

    public boolean isMoreThanSixNumbers(Collection<Integer> inputNumbers) {
        return inputNumbers.size() > SIZE_MAX;
    }
}
