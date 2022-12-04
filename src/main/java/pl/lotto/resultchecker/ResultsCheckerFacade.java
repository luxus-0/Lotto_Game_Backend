package pl.lotto.resultchecker;

import org.springframework.stereotype.Service;
import pl.lotto.resultchecker.dto.ResultsLottoDto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static pl.lotto.resultchecker.ResultsCheckerMessageProvider.NOT_WIN;
import static pl.lotto.resultchecker.ResultsCheckerMessageProvider.WIN;

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
