package pl.lotto.resultchecker;

import java.time.LocalDateTime;
import java.util.UUID;

interface ResultsCheckerRepository {
    ResultsLotto getWinnersByUUID(UUID uuid);

    ResultsLotto getWinnersByDate(LocalDateTime dateTime);

    ResultsLotto save(ResultsLotto resultsLotto);
}
