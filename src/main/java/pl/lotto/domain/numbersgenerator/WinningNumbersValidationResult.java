package pl.lotto.domain.numbersgenerator;

import lombok.Getter;

@Getter
public enum WinningNumbersValidationResult {
    WINNING_NUMBERS_NOT_FOUND("Winning inputNumbers not found"),
    NO_WINNING_TICKET("No winning ticket"),
    OUT_OF_RANGE("Out of range inputNumbers"),
    INCORRECT_SIZE("Incorrect size inputNumbers");

    private final String message;

    WinningNumbersValidationResult(String message) {
        this.message = message;
    }

}
