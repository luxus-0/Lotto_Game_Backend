package pl.lotto.domain.numbersgenerator.exceptions;

public class WinnerNumbersNotFoundException extends Exception {
    public WinnerNumbersNotFoundException() {
        super("Winning numbers not found");
    }
}
