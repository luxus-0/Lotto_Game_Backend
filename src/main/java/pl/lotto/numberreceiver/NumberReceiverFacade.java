package pl.lotto.numberreceiver;

import java.util.Collection;

public class NumberReceiverFacade {

    public static final String FAILED_MESSAGE = "failed";
    public static final String SUCCESS_MESSAGE = "success";
    private final NumberValidator numberValidator;

    public NumberReceiverFacade(NumberValidator numberValidator) {
        this.numberValidator = numberValidator;
    }

    public String inputNumbers(Collection<Integer> numbersFromUser) {
        if (numberValidator.isLessThanSixNumbers(numbersFromUser)) {
            return FAILED_MESSAGE;
        }
        return SUCCESS_MESSAGE;
    }

}
