package pl.lotto.resultannouncer;

import pl.lotto.resultannouncer.dto.ResultAnnouncerDto;
import pl.lotto.resultchecker.ResultsCheckerFacade;
import pl.lotto.resultchecker.dto.ResultsLottoDto;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class ResultAnnouncerFacade {
    private final ResultsCheckerFacade resultsCheckerFacade;

    public ResultAnnouncerFacade(ResultsCheckerFacade resultsCheckerFacade) {
        this.resultsCheckerFacade = resultsCheckerFacade;
    }

    public ResultAnnouncerDto getResultByUUID(UUID uuid) {
        ResultsLottoDto resultLotto = resultsCheckerFacade.getWinnerNumbersByUUID(uuid);
        LocalDateTime resultDateTime = resultLotto.dateTimeDraw();
        Set<Integer> resultNumbersInput = resultLotto.winnerNumbers();
        ResultAnnouncerDto resultAnnouncer = new ResultAnnouncerDto(uuid, resultNumbersInput, resultDateTime, true);
        if (!resultNumbersInput.isEmpty()) {
            return resultAnnouncer;
        }
        return Optional.of(resultAnnouncer).get();
    }
}
