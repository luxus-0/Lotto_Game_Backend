package pl.lotto.domain.numberreceiver;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static pl.lotto.domain.numberreceiver.NumbersReceiverMessageProvider.*;

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
        } else {
            messages.add(EQUALS_SIX_NUMBERS);
            return isEqualsSixNumberFrom1To99(inputNumbers);
        }
        return false;
    }


    boolean isEmptyNumbers(Set<Integer> inputNumbers) {
        return inputNumbers.isEmpty();
    }

    boolean isLessThanSixNumbers(Collection<Integer> inputNumbers) {
        return inputNumbers.size() < SIZE_MAX;
    }

    boolean isMoreThanSixNumbers(Collection<Integer> inputNumbers) {
        return inputNumbers.size() > SIZE_MAX;
    }

    boolean isEqualsSixNumberFrom1To99(Collection<Integer> inputNumbers) {
        List<Integer> rangeNumbers = inputNumbers.stream()
                .filter(number -> number >= FROM_NUMBER)
                .filter(number -> number <= TO_NUMBER)
                .toList();

        return rangeNumbers.stream().anyMatch(number -> number == SIZE_MAX);
    }
}
