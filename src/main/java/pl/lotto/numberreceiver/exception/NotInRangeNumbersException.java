package pl.lotto.numberreceiver.exception;

import pl.lotto.numberreceiver.enums.ValidateMessageInfo;

public class NotInRangeNumbersException extends RuntimeException {
    public NotInRangeNumbersException(){
        ValidateMessageInfo resultMessage = ValidateMessageInfo.NOT_IN_RANGE_NUMBERS;
        System.out.println(resultMessage);
    }
}
