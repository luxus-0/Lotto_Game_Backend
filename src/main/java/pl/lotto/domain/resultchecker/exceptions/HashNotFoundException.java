package pl.lotto.domain.resultchecker.exceptions;

public class HashNotFoundException extends RuntimeException {
    public HashNotFoundException(String message){
        super(message);
    }
}
