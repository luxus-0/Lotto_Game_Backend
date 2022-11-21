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
    private final NumberGenerator numberGenerator;

    public ResultsCheckerFacade(ResultsCheckerValidator resultsValidator, NumberGenerator numberGenerator, Clock clock) {
        this.resultsValidator = resultsValidator;
        this.numberGenerator = numberGenerator;
        this.resultsCheckerRepository = new InMemoryResultsCheckerRepository();
    }

    public ResultsLottoDto getWinnerNumbers(Set<Integer> userNumbers, LocalDateTime dateTimeDraw) {
        Set<Integer> lottoNumbers = numberGenerator.generate();
        return userNumbers.stream()
                .filter(checkWinnerNumbers -> resultsValidator.isWinnerNumbers(userNumbers, lottoNumbers))
                .map(toDto -> new ResultsLottoDto(userNumbers, dateTimeDraw, WIN))
                .findAny()
                .orElse(new ResultsLottoDto(userNumbers, dateTimeDraw, NOT_WIN));
    }

    public ResultsLottoDto getWinnerNumbersByUUID(UUID uuid) {
        if (uuid != null) {
            ResultsLotto resultsLotto = resultsCheckerRepository.getWinnersByUUID(uuid);
            ResultsLotto resultLottoCreator = new ResultsLotto(resultsLotto.uuid, resultsLotto.inputNumbers, resultsLotto.dateTimeDraw);
            ResultsLotto savedResultsLotto = resultsCheckerRepository.save(resultLottoCreator);
            return new ResultsLottoDto(savedResultsLotto.inputNumbers, savedResultsLotto.dateTimeDraw, WIN);
        }
        return new ResultsLottoDto(null, null, NOT_WIN);
    }
}
