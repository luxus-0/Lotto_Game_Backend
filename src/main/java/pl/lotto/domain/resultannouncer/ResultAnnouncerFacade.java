package pl.lotto.domain.resultannouncer;

import pl.lotto.domain.resultannouncer.dto.ResultAnnouncerDto;
import pl.lotto.domain.resultchecker.ResultsCheckerFacade;
import pl.lotto.domain.resultchecker.dto.ResultsLottoDto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public class ResultAnnouncerFacade {
    ResultsCheckerFacade resultsCheckerFacade;

    public ResultAnnouncerDto getResults(UUID uuid) {
        ResultsLottoDto resultLotto = resultsCheckerFacade.getWinnerNumbers(uuid);
        LocalDateTime resultDateTime = resultLotto.dateTimeDraw();
        Set<Integer> resultWinningNumbers = resultLotto.winnerNumbers();
        return new ResultAnnouncerDto(uuid, resultWinningNumbers, resultDateTime, ResultAnnouncerMessage.WIN);
    }
}
