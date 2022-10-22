package pl.lotto.numberreceiver;

import pl.lotto.numberreceiver.enums.ValidateMessage;

import java.util.Collections;
import java.util.List;

import static pl.lotto.numberreceiver.NumbersDuplicationInfo.countDuplicateNumbersInfo;
import static pl.lotto.numberreceiver.enums.ValidateMessage.DUPLICATE_NUMBERS;
import static pl.lotto.numberreceiver.enums.ValidateMessage.DUPLICATE_NUMBERS_NOT_FOUND;

class NumbersDuplicationCounter {
    long countDuplicatedNumbers(List<Integer> numbersInput){
        return numbersInput.stream()
                .filter(duplicateNumbers -> Collections.frequency(numbersInput, duplicateNumbers) > 1)
                .count();
    }

    ValidateMessage printInfo(List<Integer> numbersFromUser) {
        long counterNumbers = countDuplicatedNumbers(numbersFromUser);
        if (counterNumbers > 0) {
            countDuplicateNumbersInfo(counterNumbers);
            return DUPLICATE_NUMBERS;
        }
        return DUPLICATE_NUMBERS_NOT_FOUND;
    }
}
