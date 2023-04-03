package pl.lotto.domain.numbersgenerator;

public class WinningNumbersNotFoundException extends RuntimeException {
    public WinningNumbersNotFoundException(String message) {
        super(message);
    }
}
