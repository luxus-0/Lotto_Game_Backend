package pl.lotto.domain.resultchecker.exceptions;

public class PlayerResultNotFoundException extends RuntimeException {
    public PlayerResultNotFoundException(String message) {
        super(message);
    }
}
