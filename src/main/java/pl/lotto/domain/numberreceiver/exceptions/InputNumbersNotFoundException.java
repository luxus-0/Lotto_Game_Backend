package pl.lotto.domain.numberreceiver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.lotto.domain.numberreceiver.NumberReceiverValidationResult;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InputNumbersNotFoundException extends RuntimeException {
    public InputNumbersNotFoundException() {
        super(NumberReceiverValidationResult.INPUT_NUMBERS_NOT_FOUND.getInfo());
    }
}
