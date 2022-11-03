package pl.lotto.resultchecker;

import pl.lotto.numbersgenerator.WinningNumbersMessageProvider;

import java.util.Optional;
import java.util.Set;

public class ResultsNumbersFacade {
    private final ResultsChecker resultsChecker;

    public ResultsNumbersFacade(ResultsChecker resultsChecker, ResultsNumbersGenerator resultsNumbersGenerator) {
        this.resultsChecker = resultsChecker;
    }

    String getResultsNumbers(Set<Integer>inputNumbers, Set<Integer> lottoNumbers){
        if(resultsChecker.checkWinnerNumbers(inputNumbers, lottoNumbers)){
            ResultsCheckerMessageProvider messageResult = new ResultsCheckerMessageProvider(resultsChecker);
            return messageResult.getResultMessage(inputNumbers, lottoNumbers);
        }
        return ResultsCheckerMessageProvider.NOT_WIN;
    }

    ResultsLotto getAllResults(ResultsLotto results){
        if(resultsChecker.checkWinnerNumbers(results.numbersUser(), results.winningNumbers())){
            String successResult = ResultsCheckerMessageProvider.WIN;
            return new ResultsLotto(results.uuid(), results.numbersUser(), results.winningNumbers(), results.drawDate(), successResult);
        }
        return Optional.of(results).get();
    }
}
