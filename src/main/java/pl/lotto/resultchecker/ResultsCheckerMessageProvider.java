package pl.lotto.resultchecker;

import java.util.Set;

class ResultsCheckerMessageProvider {
    public final static String WIN = "win";
    public final static String NOT_WIN = "not win";
    private final ResultsChecker resultsChecker;

    ResultsCheckerMessageProvider(ResultsChecker resultsChecker) {
        this.resultsChecker = resultsChecker;
    }

    public String getResultMessage(Set<Integer> inputNumbers, Set<Integer> randomNumbers){
        if(resultsChecker.checkWinnerNumbers(inputNumbers, randomNumbers)){
            return ResultsCheckerMessageProvider.WIN;
        }
        return ResultsCheckerMessageProvider.NOT_WIN;
    }
}
