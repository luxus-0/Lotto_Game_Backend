package pl.lotto.numberreceiver;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static pl.lotto.numberreceiver.TicketMessageProvider.*;

class NumbersValidator {

    List<String> errors = new LinkedList<>();

    public boolean validate(Set<Integer> inputNumbers) {
        if (isEqualsSixNumbers(inputNumbers)) {
            System.out.println(EQUALS_SIX_NUMBERS);
        }
        if (isLessThanSixNumbers(inputNumbers)) {
            errors.add(LESS_THAN_SIX_NUMBERS);
        }
        if (isMoreThanSixNumbers(inputNumbers)) {
            errors.add(MORE_THAN_SIX_NUMBERS);
        }
        if (!isInRangeNumbers()) {
            errors.add(NOT_IN_RANGE_NUMBERS);
        }
        if (isDuplicateNumbers(inputNumbers)) {
            errors.add(DUPLICATE_NUMBERS);
        }
        if(inputNumbers.isEmpty()){
            errors.add(NUMBERS_IS_EMPTY);
        }
        throw new IllegalArgumentException("ERROR");
    }

    boolean isDuplicateNumbers(Collection<Integer> inputNumbers) {
        for (Integer number : inputNumbers) {
            if (inputNumbers.contains(number)) {
                return true;
            }
        }
        return false;
    }

    boolean isLessThanSixNumbers(Set<Integer> inputNumbers) {
        return inputNumbers.size() < SIZE_NUMBERS;
    }

    boolean isEqualsSixNumbers(Set<Integer> inputNumbers) {
        return inputNumbers.size() == SIZE_NUMBERS;
    }

    boolean isInRangeNumbers() {
        return IntStream.rangeClosed(RANGE_FROM_NUMBER, RANGE_TO_NUMBER)
                .findAny()
                .isPresent();
    }

    boolean isMoreThanSixNumbers(Set<Integer> inputNumbers) {
        return inputNumbers.size() > SIZE_NUMBERS;
    }
}
