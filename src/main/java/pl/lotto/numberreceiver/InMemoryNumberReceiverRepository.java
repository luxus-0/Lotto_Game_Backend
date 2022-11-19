package pl.lotto.numberreceiver;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

class InMemoryNumberReceiverRepository implements NumberReceiverRepository {
    private final Map<UUID, UserNumbers> inMemoryUserNumbers = new ConcurrentHashMap<>();

    @Override
    public <S extends UserNumbers> S save(S entity) {
        inMemoryUserNumbers.put(entity.uuid(), entity);
        return entity;
    }

    @Override
    public LocalDateTime findDate(LocalDateTime dateTime) {
        return inMemoryUserNumbers.values()
                .stream()
                .map(UserNumbers::dateTimeDraw)
                .filter(userNumbers -> userNumbers.equals(dateTime))
                .findAny()
                .orElse(dateTime);
    }
}

