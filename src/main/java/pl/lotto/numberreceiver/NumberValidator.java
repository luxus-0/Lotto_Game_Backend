package pl.lotto.numberreceiver;

import pl.lotto.numberreceiver.dto.NumbersResultMessageDto;
import pl.lotto.numberreceiver.dto.ValidationDto;

import java.util.Set;

import static pl.lotto.numberreceiver.NumberReceiverMessageProvider.SIZE_NUMBERS;
import static pl.lotto.numberreceiver.enums.ValidateMessageInfo.NOT_CORRECT_SIZE_NUMBERS;

class NumberValidator {
    boolean isLessThanSixNumbers(Set<Integer> numbersFromUser) {
        return numbersFromUser.stream()
                .map(valid -> new ValidationDto(numbersFromUser, NOT_CORRECT_SIZE_NUMBERS))
                .anyMatch(NumberValidator::checkSizeLessThanSixNumbers);
    }

    private static boolean checkSizeLessThanSixNumbers(ValidationDto validation) {
        return validation.numbersFromUser().size() < SIZE_NUMBERS;
    }

    public boolean isEqualsSixNumbers(Set<Integer> inputNumbers) {
        return inputNumbers.stream().allMatch(numbers -> checkEqualSixNumbers(inputNumbers));
    }

    public static boolean isSizeEqualSix(NumbersResultMessageDto correctResult) {
        return correctResult.inputNumber().size() == SIZE_NUMBERS;
    }

    private static boolean checkEqualSixNumbers(Set<Integer> inputNumbers) {
        return inputNumbers.size() == SIZE_NUMBERS;
    }

    static boolean checkMoreThanSixNumbers(Set<Integer> inputNumbers) {
        return inputNumbers.size() > SIZE_NUMBERS;
    }
}
