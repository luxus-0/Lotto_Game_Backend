package pl.lotto.resultchecker;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

interface ResultsCheckerRepository {
    Set<Integer> findWinnerNumbersByDate(LocalDateTime dateTime, Set<Integer> userNumbers);
    Set<Integer> findWinnerNumbersByUUID(UUID uuid, Set<Integer> userNumbers);
    Set<Integer> save(UUID uuid, Set<Integer> winningNumbers);
}
