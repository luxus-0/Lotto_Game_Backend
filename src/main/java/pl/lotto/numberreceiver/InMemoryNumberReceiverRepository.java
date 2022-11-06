package pl.lotto.numberreceiver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class InMemoryNumberReceiverRepository implements NumberReceiverRepository {
    private final Map<String, Set<Integer>> map = new HashMap<>();

    public Set<Integer> save(NumberReceiver numberReceiver) {
        return map.put(numberReceiver.uuid(), numberReceiver.numbersFromUser());
    }

    @Override
    public Set<Integer> findByDate(LocalDateTime drawDate) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");
        String dateTime = drawDate.format(dateFormat);
        return map.get(dateTime);
    }

    @Override
    public Set<Integer> findByUUID(String uuid) {
        return map.get(uuid);
    }
}
