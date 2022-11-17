package pl.lotto.numberreceiver;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

class InMemoryNumberReceiverRepository implements NumberReceiverRepository {
    private final Map<UUID, UserNumbers> inMemoryUserNumbers = new ConcurrentHashMap<>();

    @Override
    public <S extends UserNumbers> S save(S entity) {
        inMemoryUserNumbers.put(entity.uuid(), entity);
        return entity;
    }

    @Override
    public UserNumbers findByDate(LocalDateTime dateTime) {
        return inMemoryUserNumbers.values()
                .stream()
                .filter(userNumbers -> userNumbers.dateTimeDraw().equals(dateTime))
                .findAny()
                .orElseThrow();
    }

    @Override
    public UserNumbers findByUUID(UUID uuid) {
        return inMemoryUserNumbers.values()
                .stream()
                .filter(userNumbers -> userNumbers.uuid().equals(uuid))
                .findAny()
                .orElseThrow();
    }

    @Override
    public void delete() {
        inMemoryUserNumbers.clear();
    }
    @Override
    public void deleteByUUID(UUID uuid) {
        inMemoryUserNumbers.remove(uuid);
    }
}

