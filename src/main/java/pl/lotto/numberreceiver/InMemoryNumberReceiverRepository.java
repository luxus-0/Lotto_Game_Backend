package pl.lotto.numberreceiver;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InMemoryNumberReceiverRepository implements NumberReceiverRepository {
    private final Map<UUID, UserNumbers> databaseInMemory = new HashMap<>();
    private final Map<LocalDateTime, UserNumbers> databaseInMemory2 = new HashMap<>();

    @Override
    public UserNumbers save(UserNumbers userNumbers) {
        return databaseInMemory.put(userNumbers.uuid(), userNumbers);
    }

    @Override
    public UserNumbers findByDate(LocalDateTime dateTime) {
        return databaseInMemory2.get(dateTime);
    }

    @Override
    public UserNumbers findByUUID(UUID uuid) {
        return databaseInMemory.get(uuid);
    }
}
