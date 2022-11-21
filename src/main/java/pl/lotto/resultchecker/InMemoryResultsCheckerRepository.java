package pl.lotto.resultchecker;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static pl.lotto.resultchecker.ResultsCheckerMessageProvider.SIZE_NUMBERS;

class InMemoryResultsCheckerRepository implements ResultsCheckerRepository {
    private final Map<UUID, ResultsLotto> databaseInMemory = new ConcurrentHashMap<>();
    Clock clock;


    @Override
    public ResultsLotto getWinnersByUUID(UUID uuid, Set<Integer> userNumbers) {
        return databaseInMemory.get(uuid);
    }

    @Override
    public ResultsLotto getWinnersByDate(LocalDateTime dateTime, Set<Integer> userNumbers) {
        ResultCheckerDateTime resultDateTime = new ResultCheckerDateTime(clock);
        LocalDateTime dateTimeDraw = resultDateTime.readDateTimeDraw();
        return databaseInMemory.values()
                .stream()
                .filter(winner -> userNumbers.size() == SIZE_NUMBERS)
                .filter(checkDateDraw -> dateTime.equals(dateTimeDraw))
                .findAny()
                .orElseThrow();
    }

    @Override
    public ResultsLotto save(ResultsLotto resultsLotto) {
        return databaseInMemory.put(resultsLotto.uuid, resultsLotto);
    }
}
