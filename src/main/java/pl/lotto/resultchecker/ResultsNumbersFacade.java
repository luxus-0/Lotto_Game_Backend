package pl.lotto.resultchecker;

import pl.lotto.numberreceiver.Ticket;

import java.util.Optional;
import java.util.Set;

public class ResultsNumbersFacade {
    private final ResultsChecker resultsChecker;
    private final ResultsNumbersGenerator resultsNumbersGenerator;

    public ResultsNumbersFacade(ResultsChecker resultsChecker, ResultsNumbersGenerator resultsNumbersGenerator) {
        this.resultsChecker = resultsChecker;
        this.resultsNumbersGenerator = resultsNumbersGenerator;
    }

    String messageResultsChecker(Set<Integer>inputNumbers, Set<Integer> lottoNumbers){
        if(resultsChecker.checkWinnerNumbers(inputNumbers, lottoNumbers)){
            ResultsCheckerMessageProvider messageResult = new ResultsCheckerMessageProvider(resultsChecker);
            return messageResult.getResultMessage(inputNumbers, lottoNumbers);
        }
        return Optional.of("NOT WIN NUMBERS").orElseThrow();
    }

    String messageResultsAll(Ticket ticket, String message){

    }
}
