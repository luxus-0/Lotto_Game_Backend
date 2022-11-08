package pl.lotto.resultchecker;

import pl.lotto.numberreceiver.InMemoryNumberReceiverRepository;
import pl.lotto.numberreceiver.NumberReceiverRepository;
import pl.lotto.numberreceiver.NumbersReceiverValidator;
import pl.lotto.resultchecker.exceptions.DateWinnerNotFoundException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static pl.lotto.resultchecker.ResultsCheckerMessageProvider.WIN;
import static pl.lotto.resultchecker.ResultsCheckerMessageProvider.WINNER_NUMBERS_NOT_FOUND;

public class ResultsCheckerFacade {
    private final ResultsCheckerValidator resultsValidator;
    private final NumbersReceiverValidator receiverValidator;
    private final ResultsCheckerRepository resultsCheckerRepository;

    public ResultsCheckerFacade(ResultsCheckerValidator resultsValidator, NumbersReceiverValidator receiverValidator, ResultsCheckerRepository resultsCheckerRepository) {
        this.resultsValidator = resultsValidator;
        this.receiverValidator = receiverValidator;
        this.resultsCheckerRepository = resultsCheckerRepository;
    }

    public ResultsLotto getResults(ResultsLotto resultsLotto) {
        boolean checkWinnerNumbers = resultsValidator.isWinnerNumbers(resultsLotto.numbersUser(), resultsLotto.lottoNumbers());
        boolean checkSizeSixNumbers = receiverValidator.isEqualsSixNumbers(resultsLotto.numbersUser());
        if (checkWinnerNumbers && checkSizeSixNumbers) {
            resultsCheckerRepository.save(resultsLotto);
            return new ResultsLotto(resultsLotto.uuid(), resultsLotto.numbersUser(), resultsLotto.lottoNumbers(), resultsLotto.drawDate(), WIN);
        }
        throw new IllegalArgumentException(WINNER_NUMBERS_NOT_FOUND);
    }

    public Set<Integer> getWinnerNumbersByUUID(Set<Integer> numberInput, Set<Integer> numbersLotto, UUID uuid) {
        boolean checkWinnerNumbers = resultsValidator.isWinnerNumbers(numberInput, numbersLotto);
        Set<Integer> winNumbers = resultsCheckerRepository.findByUUID(uuid, checkWinnerNumbers);
        return new HashSet<>(winNumbers);
    }

    public Set<Integer> getWinnersNumbersByDateDraw(ResultsLotto results) {
        NumberReceiverRepository numberReceiverRepository = new InMemoryNumberReceiverRepository();
        Set<Integer> winNumbersByDate = numberReceiverRepository.findByDate(results.drawDate());
        return winNumbersByDate.stream()
                .filter(checkWin -> isWinningNumbers(results))
                .collect(Collectors.toSet());
    }

    private boolean isWinningNumbers(ResultsLotto results) {
        return resultsValidator.isWinnerNumbers(results.numbersUser(), results.lottoNumbers());
    }
}
