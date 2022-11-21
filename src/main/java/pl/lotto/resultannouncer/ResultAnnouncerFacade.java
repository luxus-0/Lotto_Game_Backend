package pl.lotto.resultannouncer;

import pl.lotto.resultannouncer.dto.ResultAnnouncerDto;
import pl.lotto.resultchecker.ResultsCheckerFacade;
import pl.lotto.resultchecker.dto.ResultsLottoDto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public class ResultAnnouncerFacade {
    private final ResultsCheckerFacade resultsCheckerFacade;

    public ResultAnnouncerFacade(ResultsCheckerFacade resultsCheckerFacade) {
        this.resultsCheckerFacade = resultsCheckerFacade;
    }

    public ResultAnnouncerDto getResultAnnouncerByUUID(UUID uuid, Set<Integer> winningNumbers) {
        ResultsLottoDto resultsLottoDto = resultsCheckerFacade.getWinnerNumbersByUUID(uuid);
        LocalDateTime dateTimeResult = resultsLottoDto.dateTimeDraw();
        boolean checkWinnerNumbers = resultsLottoDto.winnerNumbers().size() > 0;
        if (checkWinnerNumbers) {
            return new ResultAnnouncerDto(uuid, winningNumbers, dateTimeResult, checkWinnerNumbers);
        }
        return new ResultAnnouncerDto(null, null, null, false);
    }
}
