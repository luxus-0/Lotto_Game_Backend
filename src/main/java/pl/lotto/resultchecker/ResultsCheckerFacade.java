package pl.lotto.resultchecker;

import pl.lotto.resultchecker.dto.ResultsLottoDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static pl.lotto.resultchecker.ResultsCheckerMessageProvider.NOT_WIN;
import static pl.lotto.resultchecker.ResultsCheckerMessageProvider.WIN;

public class ResultsCheckerFacade {
    private final ResultsCheckerValidator resultsValidator;
    private final ResultsCheckerRepository resultsCheckerRepository;
    Clock clock;

    public ResultsCheckerFacade(ResultsCheckerValidator resultsValidator, Clock clock) {
        this.resultsValidator = resultsValidator;
        this.resultsCheckerRepository = new InMemoryResultsCheckerRepository();
        this.clock = clock;
    }

    public ResultsLottoDto getWinnerNumbers(Set<Integer> userNumbers, LocalDateTime dateTimeDraw) {
        return userNumbers.stream()
                .filter(checkWinnerNumbers -> resultsValidator.isWinnerNumbers(userNumbers))
                .map(toDto -> new ResultsLottoDto(userNumbers, dateTimeDraw, WIN))
                .findAny()
                .orElse(new ResultsLottoDto(userNumbers, dateTimeDraw, NOT_WIN));
    }

    public ResultsLottoDto getWinnerNumbers(UUID uuid) {
        ResultsLotto resultsLotto = resultsCheckerRepository.getWinnersByUUID(uuid);
        ResultsLotto resultLottoCreator = new ResultsLotto(resultsLotto.uuid, resultsLotto.inputNumbers, resultsLotto.dateTimeDraw);
        ResultsLotto savedResultsLotto = resultsCheckerRepository.save(resultLottoCreator);
        return new ResultsLottoDto(savedResultsLotto.inputNumbers, savedResultsLotto.dateTimeDraw, WIN);
    }
}
