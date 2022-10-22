package pl.lotto.numberreceiver;

import pl.lotto.numberreceiver.dto.NumbersResultMessageDto;

import java.util.Set;
import java.util.stream.IntStream;

import static pl.lotto.numberreceiver.NumbersMessageProvider.RANGE_FROM_NUMBER;
import static pl.lotto.numberreceiver.NumbersMessageProvider.RANGE_TO_NUMBER;
import static pl.lotto.numberreceiver.enums.ValidateMessage.IN_RANGE_NUMBERS;

class NumbersRangeChecker {
    boolean checkNumbersInRange(Set<Integer> numbersInRange){
        return IntStream.rangeClosed(RANGE_FROM_NUMBER, RANGE_TO_NUMBER)
                .mapToObj(rangeNumbers -> new NumbersResultMessageDto(numbersInRange, IN_RANGE_NUMBERS.name()))
                .findAny()
                .isPresent();
    }
}
