package pl.lotto.numberreceiver;

import java.util.Collection;

import static pl.lotto.numberreceiver.NumberReceiverMessageProvider.SIZE_NUMBER;

class NumberValidator {

    boolean isLessThanSixNumbers(Collection<Integer> numbersFromUser) {
        return numbersFromUser.size() < SIZE_NUMBER;
    }
}
