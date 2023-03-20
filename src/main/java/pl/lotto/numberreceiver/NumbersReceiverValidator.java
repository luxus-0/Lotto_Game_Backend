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
            messages.add(NO_NUMBERS);
        }
        else if(isNumberNotInRange(inputNumbers)){
            messages.add(OUT_OF_RANGE_NUMBERS);
        }
        else {
            messages.add(EQUALS_SIX_NUMBERS);
            return isEqualsSixNumberFrom1To99(inputNumbers);
        }
        return false;
    }


    boolean isEmptyNumbers(Set<Integer> inputNumbers) {
        return inputNumbers.size() == 0;
    }

    boolean isLessThanSixNumbers(Collection<Integer> inputNumbers) {
        return inputNumbers.size() >= MIN_NUMBER_FROM_USER && inputNumbers.size() < MAX_NUMBERS_FROM_USER;
    }

    boolean isMoreThanSixNumbers(Collection<Integer> inputNumbers) {
        return inputNumbers.size() > MAX_NUMBERS_FROM_USER;
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
                .count() == MAX_NUMBERS_FROM_USER;
    }

    boolean isNumberNotInRange(Collection<Integer> inputNumbers) {
        return inputNumbers.stream()
                .anyMatch(number -> number < MIN_NUMBER_FROM_USER || number > MAX_NUMBERS_FROM_USER);

    }
}
