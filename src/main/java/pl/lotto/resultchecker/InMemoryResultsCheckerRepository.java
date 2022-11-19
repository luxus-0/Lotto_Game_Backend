package pl.lotto.resultchecker;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.temporal.TemporalAdjusters.next;

class InMemoryResultsCheckerRepository implements ResultsCheckerRepository {
    private final Map<UUID, ResultsLotto> databaseInMemory = new ConcurrentHashMap<>();
    private final Clock clock;

    InMemoryResultsCheckerRepository(Clock clock) {
        this.clock = clock;
    }

    @Override
    public ResultsLotto findWinnerNumbersByDate(LocalDateTime dateTime, Set<Integer> userNumbers) {
       LocalDateTime dateTimeDraw =  resultDateTimeDraw();
        return databaseInMemory.values()
                .stream()
                .filter(winner -> userNumbers.size() == 6)
                .filter(checkDateDraw -> dateTime.equals(dateTimeDraw))
                .findAny()
                .orElseThrow();
    }

    @Override
    public ResultsLotto save(ResultsLotto resultsLotto) {
        return databaseInMemory.put(resultsLotto.uuid, resultsLotto);
    }

    LocalDateTime resultDateTimeDraw(){
        return LocalDateTime.now(clock)
                .with(next(SATURDAY))
                .withHour(12)
                .withMinute(0);
    }
}
