package pl.lotto.resultchecker.exceptions;

import static pl.lotto.resultchecker.ResultsCheckerMessageProvider.WINNER_NUMBERS_NOT_FOUND;

public class WinnerNumbersNotFoundException extends RuntimeException{
    public WinnerNumbersNotFoundException(){
        System.err.println(WINNER_NUMBERS_NOT_FOUND);
    }
}
