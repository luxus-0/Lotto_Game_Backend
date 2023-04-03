package pl.lotto.domain.drawdate;

public class NextDrawDateNotFoundException extends RuntimeException {
    public NextDrawDateNotFoundException(String message) {
        super(message);
    }
}
