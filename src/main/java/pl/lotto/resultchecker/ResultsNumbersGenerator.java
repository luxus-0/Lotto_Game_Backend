package pl.lotto.resultchecker;

import pl.lotto.numbersgenerator.dto.WinningNumbersResultDto;

import java.time.LocalDateTime;
import java.util.Set;

class ResultsNumbersGenerator {

    private final ResultsLotto resultsLotto;
    private final ResultsChecker resultsChecker;

    ResultsNumbersGenerator(ResultsLotto resultsLotto, ResultsChecker resultsChecker) {
        this.resultsLotto = resultsLotto;
        this.resultsChecker = resultsChecker;
    }

    public ResultsLotto generateAllResults(WinningNumbersResultDto winnerResults){
        String uuid = winnerResults.ticket().hash();
        Set<Integer> numbersFromUser = winnerResults.ticket().numbersUser();
        Set<Integer> numberWinner = resultsLotto.winningNumbers();
        ResultsCheckerMessageProvider message = new ResultsCheckerMessageProvider(resultsChecker);
        String messageResultNumbers = message.getResultMessage(numbersFromUser, numberWinner);
        LocalDateTime drawDate = winnerResults.ticket().drawDate();
        return new ResultsLotto(uuid, numbersFromUser,numberWinner, drawDate, messageResultNumbers);
    }
}
