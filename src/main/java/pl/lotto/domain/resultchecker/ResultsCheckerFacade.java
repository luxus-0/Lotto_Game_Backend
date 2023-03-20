package pl.lotto.domain.resultchecker;

import org.springframework.stereotype.Service;
import pl.lotto.domain.resultchecker.dto.ResultsLottoDto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Service
public class ResultsCheckerFacade {
    private final ResultsCheckerValidator resultsValidator;
    private final ResultsCheckerRepository resultsCheckerRepository;

    public ResultsCheckerFacade(ResultsCheckerValidator resultsValidator, ResultsCheckerRepository resultsCheckerRepository) {
        this.resultsValidator = resultsValidator;
        this.resultsCheckerRepository = resultsCheckerRepository;
    }


    public ResultsLottoDto getWinnerNumbers(Set<Integer> userNumbers, LocalDateTime dateTimeDraw) {
        return userNumbers.stream()
                .filter(checkWinnerNumbers -> resultsValidator.isWinnerNumbers(userNumbers))
                .map(toDto -> new ResultsLottoDto(userNumbers, dateTimeDraw, ResultsCheckerMessageProvider.WIN))
                .findAny()
                .orElse(new ResultsLottoDto(userNumbers, dateTimeDraw, ResultsCheckerMessageProvider.NOT_WIN));
    }

    public ResultsLottoDto getWinnerNumbers(UUID uuid) {
        ResultsLotto resultsLotto = resultsCheckerRepository.getWinnersByUUID(uuid);
        ResultsLotto resultLottoCreator = new ResultsLotto(resultsLotto.uuid, resultsLotto.inputNumbers, resultsLotto.dateTimeDraw);
        ResultsLotto savedResultsLotto = resultsCheckerRepository.save(resultLottoCreator);
        return new ResultsLottoDto(savedResultsLotto.inputNumbers, savedResultsLotto.dateTimeDraw, ResultsCheckerMessageProvider.WIN);
    }
}
