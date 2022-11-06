package pl.lotto.resultchecker;

import pl.lotto.numberreceiver.InMemoryNumberReceiverRepository;
import pl.lotto.numberreceiver.NumberReceiverRepository;
import pl.lotto.numberreceiver.dto.NumbersDateMessageDto;
import pl.lotto.resultchecker.exceptions.DateWinnerNotFoundException;

import java.util.Optional;
import java.util.Set;

import static pl.lotto.resultchecker.ResultsCheckerMessageProvider.WINNER_NUMBERS_NOT_FOUND;

public class ResultsCheckerFacade {
    private final ResultsChecker resultsChecker;

    public ResultsCheckerFacade(ResultsChecker resultsChecker) {
        this.resultsChecker = resultsChecker;
    }

    String getNumbersResult(Set<Integer> inputNumbers, Set<Integer> lottoNumbers) {
        if (resultsChecker.checkWinnerNumbers(inputNumbers, lottoNumbers)) {
            ResultsCheckerMessageProvider messageResult = new ResultsCheckerMessageProvider(resultsChecker);
            return messageResult.getResultMessage(inputNumbers, lottoNumbers);
        }
        throw new IllegalArgumentException(WINNER_NUMBERS_NOT_FOUND);
    }

    ResultsLotto getWinners(String uuid, ResultsLotto results) {
        if (resultsChecker.checkWinnerNumbers(results.numbersUser(), results.lottoNumbers())) {
            String successResult = ResultsCheckerMessageProvider.WIN;
            return new ResultsLotto(uuid, results.numbersUser(), results.lottoNumbers(), results.drawDate(), successResult);
        }
        return Optional.of(results).get();
    }

    Set<Integer> getWinnersByUUID(ResultsLotto results) {
        Set<Integer> numbersFromUser = results.numbersUser();
        Set<Integer> numbersFromLotto = results.lottoNumbers();
        if (resultsChecker.checkWinnerNumbers(numbersFromUser, numbersFromLotto)) {
            NumberReceiverRepository numberReceiverRepository = new InMemoryNumberReceiverRepository();
            return numberReceiverRepository.findByUUID(results.uuid());
        }
        throw new IllegalArgumentException();
    }

    Set<Integer> getWinnersByDateDraw(ResultsLotto results) {
        Set<Integer> numbersFromUser = results.numbersUser();
        Set<Integer> numbersFromLotto = results.lottoNumbers();
        if (resultsChecker.checkWinnerNumbers(numbersFromUser, numbersFromLotto)) {
            NumberReceiverRepository numberReceiverRepository = new InMemoryNumberReceiverRepository();
            return numberReceiverRepository.findByDate(results.drawDate());
        }
        throw new DateWinnerNotFoundException(results.drawDate());
    }
}
