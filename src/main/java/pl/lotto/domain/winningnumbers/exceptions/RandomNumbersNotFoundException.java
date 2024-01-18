package pl.lotto.domain.winningnumbers.exceptions;

public class RandomNumbersNotFoundException extends RuntimeException {
    public RandomNumbersNotFoundException() {
        super("Random numbers not found");
    }
}
