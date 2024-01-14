package pl.lotto.domain.numbersgenerator.exceptions;

public class RandomNumbersNotFoundException extends RuntimeException {
    public RandomNumbersNotFoundException() {
        super("Random numbers not found");
    }
}
