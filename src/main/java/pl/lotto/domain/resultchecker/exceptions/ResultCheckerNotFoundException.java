package pl.lotto.domain.resultchecker.exceptions;

public class ResultCheckerNotFoundException extends RuntimeException {
    public ResultCheckerNotFoundException(String message) {
        super(message);
    }
}