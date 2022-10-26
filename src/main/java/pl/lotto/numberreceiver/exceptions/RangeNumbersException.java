package pl.lotto.numberreceiver.exceptions;

import pl.lotto.numberreceiver.ValidateMessage;

public class RangeNumbersException extends RuntimeException {
    public RangeNumbersException(){
        ValidateMessage resultMessage = ValidateMessage.NOT_IN_RANGE_NUMBERS;
        System.out.println(resultMessage);
    }
}
