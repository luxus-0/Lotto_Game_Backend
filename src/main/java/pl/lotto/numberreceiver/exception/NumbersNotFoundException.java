package pl.lotto.numberreceiver.exception;

public class NumbersNotFoundException extends RuntimeException {
    public NumbersNotFoundException(){
        System.err.println("Less than six numbers not found");
    }
}
