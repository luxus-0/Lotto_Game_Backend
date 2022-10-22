package pl.lotto.numberreceiver.exception;

import pl.lotto.numberreceiver.enums.ValidateMessage;

public class RangeNumbersException extends RuntimeException {
    public RangeNumbersException(){
        ValidateMessage resultMessage = ValidateMessage.NOT_IN_RANGE_NUMBERS;
        System.out.println(resultMessage);
    }
}
