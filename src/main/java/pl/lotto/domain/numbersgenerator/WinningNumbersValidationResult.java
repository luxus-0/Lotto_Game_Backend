package pl.lotto.domain.numbersgenerator;

import lombok.Getter;

@Getter
public enum WinningNumbersValidationResult {
    WINNING_NUMBERS_NOT_FOUND("Winning numbers not found"),
    OUT_OF_RANGE("Out of range numbers"),
    INCORRECT_SIZE("Incorrect size numbers");

    private final String message;

    WinningNumbersValidationResult(String message) {
        this.message = message;
    }

}
