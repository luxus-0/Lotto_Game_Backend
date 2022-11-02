package pl.lotto.resultchecker;

import java.util.Optional;
import java.util.Set;

public class ResultsNumbersFacade {
    private final ResultsChecker resultsChecker;

    public ResultsNumbersFacade(ResultsChecker resultsChecker) {
        this.resultsChecker = resultsChecker;
    }

    String messageResultChecker(Set<Integer>inputNumbers, Set<Integer> lottoNumbers){
        if(resultsChecker.checkWinnerNumbers(inputNumbers, lottoNumbers)){
            ResultsCheckerMessageProvider messageResult = new ResultsCheckerMessageProvider(resultsChecker);
            return messageResult.getResultMessage(inputNumbers, lottoNumbers);
        }
        return Optional.of("NOT WIN NUMBERS").orElseThrow();
    }
}
