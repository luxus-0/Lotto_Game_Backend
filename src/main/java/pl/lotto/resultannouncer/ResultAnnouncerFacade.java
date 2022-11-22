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

    public ResultAnnouncerDto getResultAnnouncerByUUID(UUID uuid) {
        ResultsLottoDto resultWinner = resultsCheckerFacade.getWinnerNumbersByUUID(uuid);
        LocalDateTime winnerDateTime = resultWinner.dateTimeDraw();
        Set<Integer> winnerNumbers = resultWinner.winnerNumbers();
        ResultAnnouncerDto resultAnnouncer = new ResultAnnouncerDto(uuid, winnerNumbers, winnerDateTime, true);
        if (!winnerNumbers.isEmpty()) {
            return resultAnnouncer;
        }
        return Optional.of(resultAnnouncer).get();
    }
}
