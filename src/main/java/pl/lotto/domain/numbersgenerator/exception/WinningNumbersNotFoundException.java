package pl.lotto.domain.numbersgenerator.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Log4j2
public class WinningNumbersNotFoundException extends RuntimeException {
    public WinningNumbersNotFoundException(){
        log.error("Winning numbers not found");
    }
}