package pl.lotto.numberreceiver;

import pl.lotto.numberreceiver.enums.ValidateMessage;

import java.util.Collections;
import java.util.List;

import static pl.lotto.numberreceiver.enums.ValidateMessage.DUPLICATE_NUMBERS;
import static pl.lotto.numberreceiver.enums.ValidateMessage.DUPLICATE_NUMBERS_NOT_FOUND;

class NumbersDuplicationCounter {
    static ValidateMessage printDuplicatedNumbersInfo(List<Integer> numbersFromUser) {
        long countNumbers = countDuplicatedNumbers(numbersFromUser);
        if (countNumbers > 0) {
            countDuplicateNumbersInfo(countNumbers);
            return DUPLICATE_NUMBERS;
        }
        return DUPLICATE_NUMBERS_NOT_FOUND;
    }

    static long countDuplicatedNumbers(List<Integer> numbersInput){
        return numbersInput.stream()
                .filter(duplicateNumbers -> Collections.frequency(numbersInput, duplicateNumbers) > 1)
                .count();
    }

    static void countDuplicateNumbersInfo(long countNumbers){
        System.out.println("COUNT DUPLICATE NUMBERS: " +countNumbers);
    }
}
