package pl.lotto.numberreceiver;

import java.util.Collections;
import java.util.List;

class DuplicateNumbersCounter {
    long countIdenticalNumbers(List<Integer> numbersInput){
        return numbersInput.stream()
                .filter(duplicateNumbers -> Collections.frequency(numbersInput, duplicateNumbers) > 1)
                .count();
    }
}
