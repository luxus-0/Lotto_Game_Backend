package pl.lotto.numberreceiver;

import java.time.LocalDateTime;
import java.util.UUID;

public interface InMemoryNumberReceiverRepository {
    <S extends UserNumbers> S save(S entity);

    UserNumbers findByDate(LocalDateTime dateTime);

    UserNumbers findByUUID(UUID uuid);
}
