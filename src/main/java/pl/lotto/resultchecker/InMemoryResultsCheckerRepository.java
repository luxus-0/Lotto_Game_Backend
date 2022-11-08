package pl.lotto.resultchecker;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryResultsCheckerRepository implements ResultsCheckerRepository {
    private final Map<UUID, Set<Integer>> databaseInMemory = new ConcurrentHashMap<>();
    private final ResultsCheckerValidator validator;

    public InMemoryResultsCheckerRepository(ResultsCheckerValidator validator) {
        this.validator = validator;
    }

    @Override
    public Set<Integer> findWinnerNumbersByDate(LocalDateTime dateTime, Set<Integer> userNumbers, Set<Integer> lottoNumbers) {
        return databaseInMemory.values()
                .stream()
                .filter(winner -> validator.isWinnerNumbers(userNumbers, lottoNumbers))
                .findAny()
                .orElseThrow();
    }

    @Override
    public Set<Integer> findWinnerNumbersByUUID(UUID uuid, Set<Integer> userNumbers, Set<Integer> lottoNumbers) {
        return databaseInMemory.values()
                .stream()
                .filter(win -> validator.isWinnerNumbers(userNumbers, lottoNumbers))
                .findAny()
                .orElseGet(LinkedHashSet::new);
    }

    @Override
    public Set<Integer> save(UUID uuid, Set<Integer> winningNumbers) {
        return databaseInMemory.put(uuid, winningNumbers);
    }
}
