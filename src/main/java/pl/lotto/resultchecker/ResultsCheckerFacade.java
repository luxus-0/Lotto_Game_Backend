package pl.lotto.resultchecker;

import pl.lotto.numberreceiver.NumbersReceiverValidator;
import pl.lotto.resultchecker.dto.ResultsLotto;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toSet;
import static pl.lotto.resultchecker.ResultsCheckerMessageProvider.*;

public class ResultsCheckerFacade {
    private final ResultsCheckerValidator resultsValidator;
    private final NumbersReceiverValidator receiverValidator;
    private final ResultsCheckerRepository resultsCheckerRepository;

    public ResultsCheckerFacade(ResultsCheckerValidator resultsValidator, NumbersReceiverValidator receiverValidator, ResultsCheckerRepository resultsCheckerRepository) {
        this.resultsValidator = resultsValidator;
        this.receiverValidator = receiverValidator;
        this.resultsCheckerRepository = resultsCheckerRepository;
    }

    public Set<Integer> getWinnerNumbers(Set<Integer> userNumbers, Set<Integer> lottoNumbers) {
        return userNumbers.stream()
                .filter(winNumbers -> resultsValidator.isWinnerNumbers(userNumbers, lottoNumbers))
                .filter(isSizeSixNumbers -> receiverValidator.isEqualsSixNumbers(userNumbers))
                .collect(toSet());
        }

    public ResultsLotto getWinnerNumbersMessage(Set<Integer> userNumbers, Set<Integer> lottoNumbers) {
        Set<Integer> winnersNumbersInLotto = getWinnerNumbers(userNumbers, lottoNumbers);
        return winnersNumbersInLotto.stream()
                .map(dto -> new ResultsLotto(winnersNumbersInLotto, WIN))
                .findAny()
                .orElse(new ResultsLotto(winnersNumbersInLotto, NOT_WIN));
    }

    public Set<Integer> getWinnerNumbersByUUID(UUID uuid, Set<Integer> numbersInput, Set<Integer> numbersLotto) {
        boolean checkWinnerNumbers = resultsValidator.isWinnerNumbers(numbersInput, numbersLotto);
        Set<Integer> winNumbers = resultsCheckerRepository.findWinnerNumbersByUUID(uuid, numbersInput, numbersLotto);
        return winNumbers.stream()
                .filter(Objects::nonNull)
                .filter(isWin -> checkWinnerNumbers)
                .collect(toSet());
    }

    public Set<Integer> getWinnersNumbersByDate(LocalDateTime dateTime, Set<Integer> userNumbers, Set<Integer> lottoNumbers) {
        Set<Integer> winNumbersByDate = resultsCheckerRepository.findWinnerNumbersByDate(dateTime, userNumbers, lottoNumbers);
        return winNumbersByDate.stream()
                .filter(checkWin -> isWinningNumbers(userNumbers, lottoNumbers))
                .collect(toSet());
    }

    private boolean isWinningNumbers(Set<Integer> userNumbers, Set<Integer> lottoNumbers) {
        return resultsValidator.isWinnerNumbers(userNumbers, lottoNumbers);
    }
}
