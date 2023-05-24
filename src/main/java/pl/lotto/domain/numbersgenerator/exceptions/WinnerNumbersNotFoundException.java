package pl.lotto.domain.numbersgenerator.exceptions;

public class WinnerNumbersNotFoundException extends RuntimeException{
    public WinnerNumbersNotFoundException(String message){
        super(message);
    }
}
