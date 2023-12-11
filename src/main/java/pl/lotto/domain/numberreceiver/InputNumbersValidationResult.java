package pl.lotto.domain.numberreceiver;

import lombok.Getter;

@Getter
public enum InputNumbersValidationResult {
    LESS_THAN_SIX_NUMBERS("less than six inputNumbers"),
    MORE_THAN_SIX_NUMBERS("more than six inputNumbers"),
    OUT_OF_RANGE_NUMBERS("out of range inputNumbers"),
    EQUALS_SIX_NUMBERS("equals six inputNumbers"),
    INPUT_NUMBERS_NOT_FOUND("input inputNumbers not found");
    private final String info;

    InputNumbersValidationResult(String info) {
        this.info = info;
    }
}
