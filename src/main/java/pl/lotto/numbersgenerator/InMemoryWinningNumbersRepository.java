package pl.lotto.numbersgenerator;

import pl.lotto.numberreceiver.Ticket;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class InMemoryWinningNumbersRepository implements WinningNumbersRepository {
    private final Map<String, Set<Integer>> map = new HashMap<>();

    public Set<Integer> save(Ticket ticket) {
        return map.put(ticket.hash(), ticket.numbersUser());
    }

    @Override
    public Set<Integer> findWinningNumbers(String uuid) {
        return map.get(uuid);
    }
}
