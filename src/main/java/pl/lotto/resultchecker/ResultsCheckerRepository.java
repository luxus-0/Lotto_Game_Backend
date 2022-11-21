package pl.lotto.resultchecker;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

interface ResultsCheckerRepository {
    ResultsLotto getWinnersByUUID(UUID uuid, Set<Integer> userNumbers);

    ResultsLotto getWinnersByDate(LocalDateTime dateTime, Set<Integer> userNumbers);

    ResultsLotto save(ResultsLotto resultsLotto);
}
