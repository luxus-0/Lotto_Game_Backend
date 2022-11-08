package pl.lotto.resultchecker;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

interface ResultsCheckerRepository {
    Set<ResultsLotto> findByDate(LocalDateTime dateTime, boolean isWinner);

    Set<Integer> findByUUID(UUID uuid, boolean isWinner);

    void save(ResultsLotto resultsLotto);
}
