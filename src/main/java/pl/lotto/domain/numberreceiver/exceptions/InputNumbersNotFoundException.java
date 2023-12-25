package pl.lotto.domain.numberreceiver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InputNumbersNotFoundException extends RuntimeException {

    public InputNumbersNotFoundException(String message){
        super(message);
    }
}
