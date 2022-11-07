package pl.lotto.resultchecker;

import pl.lotto.numberreceiver.InMemoryNumberReceiverRepository;
import pl.lotto.numberreceiver.NumberReceiverRepository;
import pl.lotto.resultchecker.exceptions.DateWinnerNotFoundException;

import java.util.Set;
import java.util.UUID;

import static pl.lotto.resultchecker.ResultsCheckerMessageProvider.WINNER_NUMBERS_NOT_FOUND;

public class ResultsCheckerFacade {
    private final ResultsCheckerValidator validator;
    private final ResultsCheckerRepository resultsLottoRepository;

    public ResultsCheckerFacade(ResultsCheckerValidator validator, ResultsCheckerRepository resultsLottoRepository) {
        this.validator = validator;
        this.resultsLottoRepository = resultsLottoRepository;
    }

    public String getResults(ResultsLotto resultsLotto) {
        Set<Integer> inputNumbers = resultsLotto.numbersUser();
        Set<Integer> lottoNumbers = resultsLotto.lottoNumbers();
        boolean validation = validator.checkWinnerNumbers(inputNumbers, lottoNumbers);
        if (validation) {
            String message = ResultsCheckerMessageProvider.WIN;
            ResultsLotto results = new ResultsLotto(resultsLotto.uuid(), inputNumbers, lottoNumbers, resultsLotto.drawDate(), message);
            resultsLottoRepository.save(results);
        }
        throw new IllegalArgumentException(WINNER_NUMBERS_NOT_FOUND);
    }

    public Set<Integer> getWinnersByUUID(UUID uuid, ResultsLotto results) {
        Set<Integer> numbersFromUser = results.numbersUser();
        Set<Integer> numbersFromLotto = results.lottoNumbers();
        boolean validation = validator.checkWinnerNumbers(numbersFromUser, numbersFromLotto);
        if (validation) {
            NumberReceiverRepository numberReceiverRepository = new InMemoryNumberReceiverRepository();
            return numberReceiverRepository.findByUUID(uuid);
        }
        throw new IllegalArgumentException();
    }

    public Set<Integer> getWinnersByDateDraw(ResultsLotto results) {
        Set<Integer> numbersFromUser = results.numbersUser();
        Set<Integer> numbersFromLotto = results.lottoNumbers();
        if (validator.checkWinnerNumbers(numbersFromUser, numbersFromLotto)) {
            NumberReceiverRepository numberReceiverRepository = new InMemoryNumberReceiverRepository();
            return numberReceiverRepository.findByDate(results.drawDate());
        }
        throw new DateWinnerNotFoundException(results.drawDate());
    }
}
