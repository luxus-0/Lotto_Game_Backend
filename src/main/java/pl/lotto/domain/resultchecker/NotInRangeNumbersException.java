package pl.lotto.domain.resultchecker;

class NotInRangeNumbersException extends RuntimeException {
    public NotInRangeNumbersException(String message) {
        super(message);
    }
}
