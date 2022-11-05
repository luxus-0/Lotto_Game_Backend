package pl.lotto.resultchecker;

import java.util.Optional;
import java.util.Set;

import static pl.lotto.resultchecker.ResultsCheckerMessageProvider.WINNER_NUMBERS_NOT_FOUND;

public class ResultsCheckerFacade {
    private final ResultsChecker resultsChecker;

    public ResultsCheckerFacade(ResultsChecker resultsChecker) {
        this.resultsChecker = resultsChecker;
    }

    String getNumbersResult(Set<Integer>inputNumbers, Set<Integer> lottoNumbers){
        if(resultsChecker.checkWinnerNumbers(inputNumbers, lottoNumbers)){
            ResultsCheckerMessageProvider messageResult = new ResultsCheckerMessageProvider(resultsChecker);
            return messageResult.getResultMessage(inputNumbers, lottoNumbers);
        }
        throw new IllegalArgumentException(WINNER_NUMBERS_NOT_FOUND);
    }

    ResultsLotto getAllResults(ResultsLotto results){
        if(resultsChecker.checkWinnerNumbers(results.numbersUser(), results.winningNumbers())){
            String successResult = ResultsCheckerMessageProvider.WIN;
            return new ResultsLotto(results.uuid(), results.numbersUser(), results.winningNumbers(), results.drawDate(), successResult);
        }
        return Optional.of(results).get();
    }
}
