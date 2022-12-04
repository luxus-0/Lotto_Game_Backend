package pl.lotto.resultchecker;

import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static pl.lotto.resultchecker.ResultsCheckerMessageProvider.SIZE_NUMBERS;

@Service
class InMemoryResultsCheckerRepository implements ResultsCheckerRepository {
    private final Map<UUID, ResultsLotto> databaseInMemory = new ConcurrentHashMap<>();
    Clock clock;


    @Override
    public ResultsLotto getWinnersByUUID(UUID uuid) {
        return databaseInMemory.get(uuid);
    }

    @Override
    public ResultsLotto getWinnersByDate(LocalDateTime dateTimeDraw) {
        ResultCheckerDateTime resultDateTime = new ResultCheckerDateTime(clock);
        LocalDateTime dateTime = resultDateTime.readDateTimeDraw();
        return databaseInMemory.values()
                .stream()
                .filter(winner -> winner.inputNumbers.size() == SIZE_NUMBERS)
                .filter(drawDate -> dateTimeDraw.equals(dateTime))
                .findAny()
                .orElseThrow();
    }

    @Override
    public ResultsLotto save(ResultsLotto resultsLotto) {
        return databaseInMemory.put(resultsLotto.uuid, resultsLotto);
    }
}
