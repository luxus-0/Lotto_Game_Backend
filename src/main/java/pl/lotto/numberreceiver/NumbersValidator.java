package pl.lotto.numberreceiver;

import pl.lotto.numberreceiver.dto.NumbersResultMessageDto;
import pl.lotto.numberreceiver.dto.ValidationDto;

import java.util.Set;
import java.util.stream.IntStream;

import static pl.lotto.numberreceiver.NumbersMessageProvider.*;
import static pl.lotto.numberreceiver.enums.ValidateMessage.*;

class NumbersValidator {
    boolean checkLessThanSixNumbers(Set<Integer> numbersInput) {
        return numbersInput.stream()
                .map(valid -> new ValidationDto(numbersInput, NOT_CORRECT_SIZE_NUMBERS))
                .anyMatch(NumbersValidator::isLessThanSixSizeNumbers);
    }

    private static boolean isLessThanSixSizeNumbers(ValidationDto validator) {
        return validator.numbersFromUser().size() < SIZE_NUMBERS;
    }

    boolean checkEqualsSixNumbers(Set<Integer> inputNumbers) {
        return inputNumbers.stream().anyMatch(NumbersValidator::isCorrectSizeNumbers);
    }

    private static boolean isCorrectSizeNumbers(Integer number) {
        return number == SIZE_NUMBERS;
    }

    boolean checkMoreThanSixNumbers(Set<Integer> inputNumbers) {
        return inputNumbers.stream().anyMatch(NumbersValidator::isMoreThanCorrectSize);
    }

    boolean checkNumbersInRange(Set<Integer> numbersInRange){
        return IntStream.rangeClosed(RANGE_FROM_NUMBER, RANGE_TO_NUMBER)
                .mapToObj(rangeNumbers -> new NumbersResultMessageDto(numbersInRange, IN_RANGE_NUMBERS.name()))
                .findAny()
                .isPresent();
    }

    private static boolean isMoreThanCorrectSize(Integer number) {
        return number > SIZE_NUMBERS;
    }
}
