package pl.lotto.resultchecker;

import pl.lotto.numberreceiver.NumbersReceiverValidator;
import pl.lotto.resultchecker.dto.ResultsLotto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toSet;
import static pl.lotto.resultchecker.ResultsCheckerMessageProvider.NOT_WIN;
import static pl.lotto.resultchecker.ResultsCheckerMessageProvider.WIN;

public class ResultsCheckerFacade {
    private final ResultsCheckerValidator resultsValidator;
    private final NumbersReceiverValidator receiverValidator;
    private final ResultsCheckerRepository resultsCheckerRepository;

    public ResultsCheckerFacade(ResultsCheckerValidator resultsValidator, NumbersReceiverValidator receiverValidator, ResultsCheckerRepository resultsCheckerRepository) {
        this.resultsValidator = resultsValidator;
        this.receiverValidator = receiverValidator;
        this.resultsCheckerRepository = resultsCheckerRepository;
    }

    public ResultsLotto getWinnerNumbers(Set<Integer> userNumbers) {
        return userNumbers.stream()
                .filter(checkWinnerNumbers -> resultsValidator.isWinnerNumbers(userNumbers))
                .filter(checkSixNumbers -> receiverValidator.isEqualsSixNumbers(userNumbers))
                .map(toDto -> new ResultsLotto(userNumbers, WIN))
                .findAny()
                .orElse(new ResultsLotto(userNumbers, NOT_WIN));
    }

    public Set<Integer> getWinnerNumbersByUUID(UUID uuid, Set<Integer> numbersInput) {
        Set<Integer> winNumbers = resultsCheckerRepository.findWinnerNumbersByUUID(uuid, numbersInput);
        return winNumbers.stream()
                .filter(isWin -> resultsValidator.isWinnerNumbers(numbersInput))
                .collect(toSet());
    }

    public Set<Integer> getWinnersNumbersByDate(LocalDateTime dateTime, Set<Integer> userNumbers) {
        Set<Integer> winNumbersByDate = resultsCheckerRepository.findWinnerNumbersByDate(dateTime, userNumbers);
        return winNumbersByDate.stream()
                .filter(checkWin -> resultsValidator.isWinnerNumbers(userNumbers))
                .collect(toSet());
    }

    public Set<Integer> addWinnerNumbers(UUID uuid, Set<Integer> inputNumbers) {
        ResultsLotto winnerNumbers = getWinnerNumbers(inputNumbers);
        Set<Integer> winNumbers = resultsCheckerRepository.findWinnerNumbersByUUID(uuid, winnerNumbers.resultNumbers());
        return resultsCheckerRepository.save(uuid, winNumbers);
    }
}
