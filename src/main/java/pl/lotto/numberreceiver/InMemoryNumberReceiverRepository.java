package pl.lotto.numberreceiver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class InMemoryNumberReceiverRepository implements NumberReceiverRepository {
    private final Map<UUID, Set<Integer>> databaseInMemory = new HashMap<>();
    private final Map<LocalDateTime, Set<Integer>> dateTimeInMemory = new HashMap<>();

    @Override
    public NumberReceiver save(NumberReceiver numberReceiver) {
        databaseInMemory.put(numberReceiver.uuid(), numberReceiver.numbersFromUser());
        dateTimeInMemory.put(numberReceiver.drawDate(), numberReceiver.numbersFromUser());
        return new NumberReceiver(numberReceiver.uuid(), numberReceiver.numbersFromUser(), numberReceiver.drawDate());
    }

    @Override
    public Set<Integer> findByDate(String dateTime) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");
        LocalDateTime drawDateTime = LocalDateTime.parse(dateTime, dateFormat);
        return dateTimeInMemory.get(drawDateTime);
    }

    @Override
    public Set<Integer> findByUUID(UUID uuid) {
        return databaseInMemory.get(uuid);
    }
}
