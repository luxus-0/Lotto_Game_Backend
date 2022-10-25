package pl.lotto.numberreceiver;

import java.util.List;
import java.util.Set;

class TicketMessageProvider {
    public static final int SIZE_NUMBERS = 6;
    public static final int RANGE_FROM_NUMBER = 1;
    public static final int RANGE_TO_NUMBER = 99;
    public static final String LESS_THAN_SIX_NUMBERS = "less than six numbers";
    public static final String MORE_THAN_SIX_NUMBERS = "more than six numbers";
    public static final String EQUALS_SIX_NUMBERS = "equals six numbers";
    public static final String NOT_IN_RANGE_NUMBERS = "not in range numbers";
    public static final String DUPLICATE_NUMBERS = "duplicate numbers";
    public static final String NUMBERS_IS_EMPTY = "numbers is empty";
    public static final String GENERATED_TICKET_OK = "ok";
    public static final String GENERATED_TICKET_FAILED = "failed";

    static String ticket_failed() {
        return GENERATED_TICKET_FAILED;
    }

    static String ticket_ok() {
        return GENERATED_TICKET_OK;
    }
}
