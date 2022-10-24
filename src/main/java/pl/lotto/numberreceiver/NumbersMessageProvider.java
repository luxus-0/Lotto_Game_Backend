package pl.lotto.numberreceiver;

class NumbersMessageProvider {
    public static final int SIZE_NUMBERS = 6;
    public static final int RANGE_FROM_NUMBER = 1;
    public static final int RANGE_TO_NUMBER = 99;
    public static final String FAILED_MESSAGE = "failed";
    public static final String SUCCESS_MESSAGE = "success";
    public static final String INVALID_MESSAGE = "invalid";

    static void numbers_not_found(){
        System.err.println("Numbers not found");
    }
}
