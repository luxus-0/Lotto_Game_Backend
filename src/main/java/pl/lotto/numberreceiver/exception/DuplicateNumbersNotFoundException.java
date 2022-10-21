package pl.lotto.numberreceiver.exception;

public class DuplicateNumbersNotFoundException extends RuntimeException {
    public DuplicateNumbersNotFoundException(){
        System.err.println("DUPLICATE NUMBERS NOT FOUND");
    }
}
