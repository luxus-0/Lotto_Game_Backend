package pl.lotto.resultchecker;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryResultsCheckerRepository implements ResultsCheckerRepository {
    private final Map<UUID, ResultsLotto> databaseInMemory = new ConcurrentHashMap<>();

    private static boolean isWinnerMessage(ResultsLotto resultsLotto) {
        return resultsLotto.message().equals(ResultsCheckerMessageProvider.WIN);
    }

    @Override
    public Set<ResultsLotto> findByDate(LocalDateTime dateTime, boolean isWinner) {
        return databaseInMemory.values()
                .stream()
                .filter(resultsLotto -> resultsLotto.drawDate().equals(dateTime))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<ResultsLotto> findByUUID(UUID uuid, boolean isWinner) {
        return databaseInMemory.values()
                .stream()
                .filter(InMemoryResultsCheckerRepository::isWinnerMessage)
                .collect(Collectors.toSet());
    }

    @Override
    public ResultsLotto save(ResultsLotto results) {
        return databaseInMemory.put(results.uuid(), results);
    }
}
