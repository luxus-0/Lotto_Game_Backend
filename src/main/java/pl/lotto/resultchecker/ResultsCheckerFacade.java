package pl.lotto.resultchecker;

import pl.lotto.resultchecker.dto.ResultsLottoDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Set;

import static pl.lotto.resultchecker.ResultsCheckerMessageProvider.NOT_WIN;
import static pl.lotto.resultchecker.ResultsCheckerMessageProvider.WIN;

public class ResultsCheckerFacade {
    private final ResultsCheckerValidator resultsValidator;
    private final ResultsCheckerRepository resultsCheckerRepository;

    public ResultsCheckerFacade(ResultsCheckerValidator resultsValidator, Clock clock) {
        this.resultsValidator = resultsValidator;
        this.resultsCheckerRepository = new InMemoryResultsCheckerRepository(clock);
    }

    public ResultsLottoDto getWinnerNumbers(Set<Integer> numbers) {
        return numbers.stream()
                .filter(checkWinnerNumbers -> resultsValidator.isWinnerNumbers(numbers))
                .map(toDto -> new ResultsLottoDto(numbers, WIN))
                .findAny()
                .orElse(new ResultsLottoDto(numbers, NOT_WIN));
    }

    public ResultsLottoDto getWinnerNumbersByDate(LocalDateTime dateTime, Set<Integer> userNumbers) {
        if (dateTime != null && userNumbers != null) {
            ResultsLotto resultsLotto = resultsCheckerRepository.getWinnersByDate(dateTime, userNumbers);
            ResultsLotto resultLottoCreator = new ResultsLotto(resultsLotto.uuid, resultsLotto.inputNumbers, resultsLotto.dateTimeDraw);
            ResultsLotto savedResultsLotto = resultsCheckerRepository.save(resultLottoCreator);
            return new ResultsLottoDto(savedResultsLotto.inputNumbers, WIN);
        }
        return new ResultsLottoDto(null, NOT_WIN);
    }
}
