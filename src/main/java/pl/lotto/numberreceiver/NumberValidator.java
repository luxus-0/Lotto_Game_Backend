package pl.lotto.numberreceiver;

import java.util.Collection;

class NumberValidator {

    public static final int MAXIMUM_NUMBERS_FROM_USER = 6;

    boolean isLessThanSixNumbers(Collection<Integer> numbersFromUser) {
        return numbersFromUser.size() < MAXIMUM_NUMBERS_FROM_USER;
    }
}
