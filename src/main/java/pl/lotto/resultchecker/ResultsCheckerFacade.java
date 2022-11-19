package pl.lotto.resultchecker;

import pl.lotto.resultchecker.dto.ResultsLottoDto;

import java.time.LocalDateTime;
import java.util.Set;

import static pl.lotto.resultchecker.ResultsCheckerMessageProvider.NOT_WIN;
import static pl.lotto.resultchecker.ResultsCheckerMessageProvider.WIN;

public class ResultsCheckerFacade {
    private final ResultsCheckerValidator resultsValidator;
    private final ResultsCheckerRepository resultsCheckerRepository;

    public ResultsCheckerFacade(ResultsCheckerValidator resultsValidator, ResultsCheckerRepository resultsCheckerRepository) {
        this.resultsValidator = resultsValidator;
        this.resultsCheckerRepository = resultsCheckerRepository;
    }

    public ResultsLottoDto getWinnerNumbers(Set<Integer> userNumbers) {
        return userNumbers.stream()
                .filter(checkWinnerNumbers -> resultsValidator.isWinnerNumbers(userNumbers))
                .filter(checkSixNumbers -> userNumbers.size() == 6)
                .map(toDto -> new ResultsLottoDto(userNumbers, WIN))
                .findAny()
                .orElse(new ResultsLottoDto(userNumbers, NOT_WIN));
    }

    public ResultsLottoDto getWinnersNumbersByDate(LocalDateTime dateTime, Set<Integer> userNumbers) {
        ResultsLotto resultsLotto = resultsCheckerRepository.findWinnerNumbersByDate(dateTime, userNumbers);
        ResultsLotto resultLottoCreator = new ResultsLotto(resultsLotto.uuid, resultsLotto.inputNumbers, resultsLotto.dateTimeDraw);
        ResultsLotto savedResultsLotto = resultsCheckerRepository.save(resultLottoCreator);
        return new ResultsLottoDto(savedResultsLotto.inputNumbers, WIN);
    }
}
