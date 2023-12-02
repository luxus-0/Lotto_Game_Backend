package pl.lotto.domain.numberreceiver;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

class NumbersReceiverValidator {

    private static final int QUANTITY_NUMBERS_FROM_USER = 6;
    private static final int MIN_NUMBER_FROM_USER = 1;
    private static final int MAX_NUMBER_FROM_USER = 99;

    List<TicketValidationResult> errors;

    boolean validate(Set<Integer> inputNumbers) {
        errors = new LinkedList<>();
        if (inputNumbers == null) {
            throw new RuntimeException("InputNumbers must not be null");
        } else if (inputNumbers.isEmpty()) {
            throw new RuntimeException("InputNumbers must not be empty");
        } else if (isEqualsSixNumberFrom1To99(inputNumbers)) {
            errors.add(TicketValidationResult.EQUALS_SIX_NUMBERS);
            return true;
        } else {
            if (isLessThanSixNumbers(inputNumbers)) {
                errors.add(TicketValidationResult.LESS_THAN_SIX_NUMBERS);
            }
            if (isMoreThanSixNumbers(inputNumbers)) {
                errors.add(TicketValidationResult.MORE_THAN_SIX_NUMBERS);
            }
            if (isOutOfRange(inputNumbers)) {
                errors.add(TicketValidationResult.OUT_OF_RANGE_NUMBERS);
            }
        }
        return false;
    }

    boolean isLessThanSixNumbers(Set<Integer> inputNumbers) {
        return inputNumbers.size() > MIN_NUMBER_FROM_USER && inputNumbers.size() < QUANTITY_NUMBERS_FROM_USER;
    }

    boolean isMoreThanSixNumbers(Set<Integer> inputNumbers) {
        return inputNumbers.size() > QUANTITY_NUMBERS_FROM_USER;
    }

    boolean isEqualsSixNumberFrom1To99(Set<Integer> inputNumbers) {
        return inputNumbers.stream()
                .filter(number -> number >= MIN_NUMBER_FROM_USER)
                .filter(number -> number <= MAX_NUMBER_FROM_USER)
                .count() == QUANTITY_NUMBERS_FROM_USER;
    }

    boolean isOutOfRange(Set<Integer> inputNumbers) {
        return inputNumbers.stream()
                .anyMatch(number -> number < MIN_NUMBER_FROM_USER || number > MAX_NUMBER_FROM_USER);
    }

    public String getMessage() {
        return errors.stream()
                .map(TicketValidationResult::getInfo)
                .findAny()
                .orElseThrow(() -> new RuntimeException("InputNumbers must not be empty"));
    }
}
