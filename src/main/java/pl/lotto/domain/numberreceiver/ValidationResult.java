package pl.lotto.domain.numberreceiver;

import lombok.Getter;

@Getter
public enum ValidationResult {
    LESS_THAN_SIX_NUMBERS("less than six numbers"),
    MORE_THAN_SIX_NUMBERS("more than six numbers"),
    OUT_OF_RANGE_NUMBERS("out of range numbers"),
    EMPTY_NUMBERS("empty numbers"),
    EQUALS_SIX_NUMBERS("equals six numbers");
    private final String info;

    ValidationResult(String info) {
        this.info = info;
    }
}
