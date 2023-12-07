package pl.lotto.domain.numberreceiver;

import lombok.Getter;

@Getter
public enum InputNumbersValidationResult {
    LESS_THAN_SIX_NUMBERS("less than six numbers"),
    MORE_THAN_SIX_NUMBERS("more than six numbers"),
    OUT_OF_RANGE_NUMBERS("out of range numbers"),
    EQUALS_SIX_NUMBERS("equals six numbers"),
    INPUT_NUMBERS_NOT_FOUND("input numbers not found");
    private final String info;

    InputNumbersValidationResult(String info) {
        this.info = info;
    }
}
