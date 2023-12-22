package pl.lotto.domain.drawdate.exceptions;

public class IncorrectDrawDateException extends RuntimeException{
    public IncorrectDrawDateException(String message){
        super(message);
    }
}
