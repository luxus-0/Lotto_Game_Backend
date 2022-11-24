package pl.lotto.numberreceiver;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

class InMemoryNumberReceiverRepository implements NumberReceiverRepository {
    private final Map<UUID, UserNumbers> inMemoryUserNumbers = new ConcurrentHashMap<>();
    private final DateTimeDrawGenerator dateTimeDraw;

    InMemoryNumberReceiverRepository(DateTimeDrawGenerator dateTimeDraw) {
        this.dateTimeDraw = dateTimeDraw;
    }

    @Override
    public <S extends UserNumbers> S save(S entity) {
        inMemoryUserNumbers.put(entity.uuid(), entity);
        return entity;
    }

    @Override
    public LocalDateTime findByDate(LocalDateTime dateTime) {
        LocalDateTime drawDate = dateTimeDraw.generateNextDrawDate();
        return inMemoryUserNumbers.values()
                .stream()
                .map(UserNumbers::dateTimeDraw)
                .filter(dateTimeDraw -> dateTime.equals(drawDate))
                .findAny()
                .orElse(dateTime);
    }

    @Override
    public UserNumbers findByUUID(UUID uuid) {
        return inMemoryUserNumbers.get(uuid);
    }
}

