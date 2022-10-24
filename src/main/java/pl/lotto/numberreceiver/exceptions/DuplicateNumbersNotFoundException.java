package pl.lotto.numberreceiver.exceptions;

public class DuplicateNumbersNotFoundException extends RuntimeException {
    public DuplicateNumbersNotFoundException(){
        System.err.println("DUPLICATE NUMBERS NOT FOUND");
    }
}
