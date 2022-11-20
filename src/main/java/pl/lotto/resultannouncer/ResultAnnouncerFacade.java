package pl.lotto.resultannouncer;

import pl.lotto.resultchecker.ResultsCheckerFacade;
import pl.lotto.resultchecker.dto.ResultsLottoDto;

import java.util.Set;
import java.util.UUID;

public class ResultAnnouncerFacade {
    private final ResultsCheckerFacade resultsCheckerFacade;

    public ResultAnnouncerFacade(ResultsCheckerFacade resultsCheckerFacade) {
        this.resultsCheckerFacade = resultsCheckerFacade;
    }

    String getResultAnnouncerByUUID(UUID uuid, Set<Integer> winningNumbers){
        ResultsLottoDto resultsLottoDto = resultsCheckerFacade.getWinnerNumbersByUUID(uuid, winningNumbers);
        return resultsLottoDto.message();
    }
}
