package pl.lotto.domain.numbersgenerator.exception;

import lombok.extern.log4j.Log4j2;
@Log4j2
public class RandomNumbersNotFoundException extends RuntimeException{
    public RandomNumbersNotFoundException(){
        log.error("Random numbers not found");
    }
}
