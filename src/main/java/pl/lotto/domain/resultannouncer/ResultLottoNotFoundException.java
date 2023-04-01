package pl.lotto.domain.resultannouncer;

public class ResultLottoNotFoundException extends RuntimeException {
    public ResultLottoNotFoundException(String message) {
        super(message);
    }
}
