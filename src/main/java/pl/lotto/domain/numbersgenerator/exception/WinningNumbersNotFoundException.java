package pl.lotto.domain.numbersgenerator.exception;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class WinningNumbersNotFoundException extends RuntimeException {
    public WinningNumbersNotFoundException(String message){
        super(message);
    }
}
