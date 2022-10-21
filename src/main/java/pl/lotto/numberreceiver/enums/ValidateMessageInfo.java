package pl.lotto.numberreceiver.enums;

public enum ValidateMessageInfo {
    CORRECT_SIZE_NUMBERS("CORRECT SIZE"),
    NOT_CORRECT_SIZE_NUMBERS("NOT CORRECT SIZE"),
    UNKNOWN_SIZE_NUMBERS("UNKNOWN SIZE"),
    DUPLICATE_NUMBERS("DUPLICATE NUMBERS"),
    NOT_IN_RANGE_NUMBERS("NOT IN RANGE NUMBERS");

    ValidateMessageInfo(String info) {
    }
}
