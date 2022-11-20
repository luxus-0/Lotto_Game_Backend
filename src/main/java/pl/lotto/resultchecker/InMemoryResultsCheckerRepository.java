package pl.lotto.resultchecker;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static pl.lotto.resultchecker.ResultsCheckerMessageProvider.SIZE_NUMBERS;

class InMemoryResultsCheckerRepository implements ResultsCheckerRepository {
    private final Map<UUID, ResultsLotto> databaseInMemory = new ConcurrentHashMap<>();
    private final ResultCheckerDateTime resultDateTime;

    InMemoryResultsCheckerRepository(ResultCheckerDateTime resultDateTime) {
        this.resultDateTime = resultDateTime;
    }


    @Override
    public ResultsLotto getWinnersByDate(LocalDateTime dateTime, Set<Integer> userNumbers) {
            LocalDateTime dateTimeDraw =  resultDateTime.readDateTimeDraw();
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
