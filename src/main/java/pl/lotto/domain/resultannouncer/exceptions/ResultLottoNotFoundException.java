package pl.lotto.domain.resultannouncer.exceptions;

public class ResultLottoNotFoundException extends RuntimeException {
    public ResultLottoNotFoundException(String message) {
        super(message);
    }
}
