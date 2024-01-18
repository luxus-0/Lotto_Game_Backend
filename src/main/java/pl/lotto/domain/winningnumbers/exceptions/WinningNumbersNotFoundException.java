package pl.lotto.domain.winningnumbers.exceptions;

public class WinningNumbersNotFoundException extends RuntimeException {
    public WinningNumbersNotFoundException() {
        super("Winning numbers not found");
    }
}
