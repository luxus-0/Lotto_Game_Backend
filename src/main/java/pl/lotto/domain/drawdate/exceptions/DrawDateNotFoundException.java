package pl.lotto.domain.drawdate.exceptions;

public class DrawDateNotFoundException extends RuntimeException {
    public DrawDateNotFoundException(String message) {
        super(message);
    }
}
