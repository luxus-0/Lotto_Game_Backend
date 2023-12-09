package pl.lotto.domain.numberreceiver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static pl.lotto.domain.numberreceiver.InputNumbersValidationResult.INPUT_NUMBERS_NOT_FOUND;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InputNumbersNotFoundException extends RuntimeException {
    public InputNumbersNotFoundException() {
        super(INPUT_NUMBERS_NOT_FOUND.getInfo());
    }

    public InputNumbersNotFoundException(String message){
        super(message);
    }
}
