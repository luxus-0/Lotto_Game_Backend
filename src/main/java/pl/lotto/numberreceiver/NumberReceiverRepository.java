package pl.lotto.numberreceiver;

import java.time.LocalDateTime;
import java.util.UUID;

public interface NumberReceiverRepository {
    <S extends UserNumbers> S save(S entity);

    LocalDateTime findByDate(LocalDateTime dateTime);

    UserNumbers findByUUID(UUID uuid);
}
