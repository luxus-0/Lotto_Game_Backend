package pl.lotto.resultchecker;

import pl.lotto.numbersgenerator.dto.WinningNumbersResultDto;

import java.time.LocalDateTime;
import java.util.Set;

class ResultsLottoGenerator {

    private final ResultsChecker resultsChecker;
    private final ResultsLotto resultsLotto;

    ResultsLottoGenerator(ResultsChecker resultsChecker, ResultsLotto resultsLotto) {
        this.resultsChecker = resultsChecker;
        this.resultsLotto = resultsLotto;
    }

    public ResultsLotto generateAllResults(WinningNumbersResultDto winnerResults){
        String uuid = winnerResults.ticket().hash();
        Set<Integer> numbersFromUser = winnerResults.ticket().numbersUser();
        Set<Integer> numberWinner = resultsLotto.getWinningNumbers();
        boolean isWinner = resultsChecker.checkWinnerNumbers(numbersFromUser, resultsLotto.getWinningNumbers());
        LocalDateTime drawDate = winnerResults.ticket().drawDate();
        return new ResultsLotto(uuid, numbersFromUser,numberWinner, drawDate, isWinner);
    }
}
