package pl.lotto.resultchecker;

import java.util.Set;

class ResultsCheckerMessageProvider {
    public final static String WIN = "win";
    public final static String NOT_WIN = "not win";
    public final static String WINNER_NUMBERS_NOT_FOUND = "results not found";

    ResultsCheckerMessageProvider(ResultsCheckerValidator resultsChecker) {
        this.resultsChecker = resultsChecker;
    }
}
