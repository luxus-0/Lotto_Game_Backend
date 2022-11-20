package pl.lotto.resultchecker;

import pl.lotto.resultchecker.dto.ResultsLottoDto;

import java.time.LocalDateTime;
import java.util.Set;

import static pl.lotto.resultchecker.ResultsCheckerMessageProvider.NOT_WIN;
import static pl.lotto.resultchecker.ResultsCheckerMessageProvider.WIN;

public class ResultsCheckerFacade {
    private static final int SIX_NUMBERS = 6;
    private final ResultsCheckerValidator resultsValidator;
    private final ResultsCheckerRepository resultsCheckerRepository;

    public ResultsCheckerFacade(ResultsCheckerValidator resultsValidator, ResultsCheckerRepository resultsCheckerRepository) {
        this.resultsValidator = resultsValidator;
        this.resultsCheckerRepository = resultsCheckerRepository;
    }

    public ResultsLottoDto getWinnerNumbers(Set<Integer> numbers) {
        return numbers.stream()
                .filter(checkWinnerNumbers -> resultsValidator.isWinnerNumbers(numbers))
                .map(toDto -> new ResultsLottoDto(numbers, WIN))
                .findAny()
                .orElse(new ResultsLottoDto(numbers, NOT_WIN));
    }

    public ResultsLottoDto getWinnersByDate(LocalDateTime dateTime, Set<Integer> userNumbers) {
        ResultsLotto resultsLotto = resultsCheckerRepository.getWinnersByDate(dateTime, userNumbers);
        ResultsLotto resultLottoCreator = new ResultsLotto(resultsLotto.uuid, resultsLotto.inputNumbers, resultsLotto.dateTimeDraw);
        ResultsLotto savedResultsLotto = resultsCheckerRepository.save(resultLottoCreator);
        return new ResultsLottoDto(savedResultsLotto.inputNumbers, WIN);
    }
}
