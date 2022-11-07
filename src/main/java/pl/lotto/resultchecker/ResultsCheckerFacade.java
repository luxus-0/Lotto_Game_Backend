package pl.lotto.resultchecker;

import pl.lotto.numberreceiver.InMemoryNumberReceiverRepository;
import pl.lotto.numberreceiver.NumberReceiverRepository;
import pl.lotto.numberreceiver.NumbersReceiverValidator;
import pl.lotto.resultchecker.exceptions.DateWinnerNotFoundException;

import java.util.Set;
import java.util.UUID;

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

    public ResultsLotto getAllResults(ResultsLotto resultsLotto) {
        boolean isWinnerNumbers = containWinnerNumbers(resultsLotto);
        boolean isSizeSixNumbers = isEqualsSixNumbers(resultsLotto);
        if (isWinnerNumbers && isSizeSixNumbers) {
            return new ResultsLotto(resultsLotto.uuid(), resultsLotto.numbersUser(), resultsLotto.lottoNumbers(), resultsLotto.drawDate(), ResultsCheckerMessageProvider.WIN);
        }
        throw new IllegalArgumentException(WINNER_NUMBERS_NOT_FOUND);
    }

    private boolean isEqualsSixNumbers(ResultsLotto resultsLotto) {
        return receiverValidator.isEqualsSixNumbers(resultsLotto.numbersUser());
    }

    private boolean containWinnerNumbers(ResultsLotto resultsLotto) {
        return resultsValidator.checkWinnerNumbers(resultsLotto.numbersUser(), resultsLotto.lottoNumbers());
    }

    public Set<Integer> getResultsByUUID(String uuid, ResultsLotto results) {
        if (containWinnerNumbers(results) && isEqualsSixNumbers(results)) {
            NumberReceiverRepository numberReceiverRepository = new InMemoryNumberReceiverRepository();
            return numberReceiverRepository.findByUUID(uuid);
        }
        throw new IllegalArgumentException();
    }

    public Set<Integer> getWinnersByDateDraw(ResultsLotto results) {
        if (containWinnerNumbers(results) && isEqualsSixNumbers(results)) {
            NumberReceiverRepository numberReceiverRepository = new InMemoryNumberReceiverRepository();
            return numberReceiverRepository.findByDate(results.drawDate());
        }
        throw new DateWinnerNotFoundException(results.drawDate());
    }
}
