package pl.lotto.resultchecker;

import pl.lotto.numbersgenerator.dto.WinningNumbersResultDto;

import java.time.LocalDateTime;
import java.util.Set;

class ResultsNumbersGenerator {

    private final ResultsChecker resultsChecker;
    private final ResultsLotto resultsLotto;
    private final ResultsCheckerMessageProvider resultMessage;

    ResultsNumbersGenerator(ResultsChecker resultsChecker, ResultsLotto resultsLotto, ResultsCheckerMessageProvider resultMessage) {
        this.resultsChecker = resultsChecker;
        this.resultsLotto = resultsLotto;
        this.resultMessage = resultMessage;
    }

    public ResultsLotto generateAllResults(WinningNumbersResultDto winnerResults){
        String uuid = winnerResults.ticket().hash();
        Set<Integer> numbersFromUser = winnerResults.ticket().numbersUser();
        Set<Integer> numberWinner = resultsLotto.winningNumbers();
        boolean isWinner = resultsChecker.checkWinnerNumbers(numbersFromUser, resultsLotto.winningNumbers());
        String messageResultNumbers = resultMessage.getResultMessage(numbersFromUser, numberWinner);
        LocalDateTime drawDate = winnerResults.ticket().drawDate();
        return new ResultsLotto(uuid, numbersFromUser,numberWinner, drawDate, isWinner, messageResultNumbers);
    }
}
