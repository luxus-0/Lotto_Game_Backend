package pl.lotto.numberreceiver.exceptions;

public class NumbersNotFoundException extends RuntimeException {
    public NumbersNotFoundException(){
        System.err.println("Less than six numbers not found");
    }
}
