package pl.lotto.domain.numbersgenerator.exception;

public class OutOfRangeNumbersException extends RuntimeException {
    public OutOfRangeNumbersException(String message) {
        super(message);
    }
}
