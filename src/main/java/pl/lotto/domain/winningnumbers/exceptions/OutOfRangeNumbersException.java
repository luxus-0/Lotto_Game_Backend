package pl.lotto.domain.winningnumbers.exceptions;

public class OutOfRangeNumbersException extends RuntimeException {
    public OutOfRangeNumbersException(String message) {
        super(message);
    }
}
