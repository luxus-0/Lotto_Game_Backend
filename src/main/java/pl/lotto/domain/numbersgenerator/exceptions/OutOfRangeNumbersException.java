package pl.lotto.domain.numbersgenerator.exceptions;

public class OutOfRangeNumbersException extends RuntimeException {
    public OutOfRangeNumbersException(String message) {
        super(message);
    }
}
