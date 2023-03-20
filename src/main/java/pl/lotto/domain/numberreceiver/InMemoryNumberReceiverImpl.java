package pl.lotto.domain.numberreceiver;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

class InMemoryNumberReceiverImpl implements InMemoryNumberReceiverRepository {
    private final Map<UUID, UserNumbers> inMemoryUserNumbers = new ConcurrentHashMap<>();
    Clock clock = Clock.systemDefaultZone();
    DateTimeDrawGenerator dateTimeDrawGenerator = new DateTimeDrawGenerator(clock);

    @Override
    public <S extends UserNumbers> S save(S entity) {
        inMemoryUserNumbers.put(entity.uuid(), entity);
        return entity;
    }

    @Override
    public UserNumbers findByDate(LocalDateTime dateTime) {
        UUID uuid = UUID.randomUUID();
        LocalDateTime localDateTime = dateTimeDrawGenerator.generateNextDrawDate();
        if (dateTime != null) {
            return new UserNumbers(uuid, Set.of(), localDateTime);
        }
        return new UserNumbers(null, null, null);
    }

    @Override
    public UserNumbers findByUUID(UUID uuid) {
        return inMemoryUserNumbers.get(uuid);
    }
}

