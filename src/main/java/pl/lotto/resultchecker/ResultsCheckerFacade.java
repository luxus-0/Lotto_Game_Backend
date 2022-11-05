package pl.lotto.resultchecker;

import pl.lotto.numbersgenerator.InMemoryWinningNumbersRepository;
import pl.lotto.numbersgenerator.WinningNumbersRepository;
import pl.lotto.numbersgenerator.dto.WinningNumbersResultDto;

import java.util.Optional;
import java.util.Set;

import static pl.lotto.resultchecker.ResultsCheckerMessageProvider.WINNER_NUMBERS_NOT_FOUND;

public class ResultsCheckerFacade {
    private final ResultsChecker resultsChecker;
    private final WinningNumbersResultDto winningNumbersResult;

    public ResultsCheckerFacade(ResultsChecker resultsChecker, WinningNumbersResultDto winningNumbersResult) {
        this.resultsChecker = resultsChecker;
        this.winningNumbersResult = winningNumbersResult;
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

    Set<Integer> getWinners(String uuid, ResultsLotto results){
        Set<Integer> numbersFromUser = results.numbersUser();
        Set<Integer> winnerNumbers = results.winningNumbers();
        if(resultsChecker.checkWinnerNumbers(numbersFromUser, winnerNumbers)){
            WinningNumbersRepository winningNumbersRepository = new InMemoryWinningNumbersRepository();
            return winningNumbersRepository.findWinningNumbers(uuid);
        }
        return Optional.of(results.winningNumbers()).orElseThrow();
    }
}
