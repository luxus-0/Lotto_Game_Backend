package pl.lotto.resultannouncer;

import pl.lotto.resultannouncer.dto.ResultAnnouncerDto;
import pl.lotto.resultchecker.ResultsCheckerFacade;
import pl.lotto.resultchecker.dto.ResultsLottoDto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static pl.lotto.resultannouncer.ResultAnnouncerMessage.WIN;

public class ResultAnnouncerFacade {
    ResultsCheckerFacade resultsCheckerFacade;

    public ResultAnnouncerDto getResults(UUID uuid) {
        ResultsLottoDto resultLotto = resultsCheckerFacade.getWinnerNumbers(uuid);
        LocalDateTime resultDateTime = resultLotto.dateTimeDraw();
        Set<Integer> resultWinningNumbers = resultLotto.winnerNumbers();
        return new ResultAnnouncerDto(uuid, resultWinningNumbers, resultDateTime, WIN);
    }
}
