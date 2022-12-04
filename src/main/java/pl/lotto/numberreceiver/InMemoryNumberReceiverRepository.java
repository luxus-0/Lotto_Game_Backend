package pl.lotto.numberreceiver;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface InMemoryNumberReceiverRepository{
    <S extends UserNumbers> S save(S entity);

    LocalDateTime findByDate(LocalDateTime dateTime);

    UserNumbers findByUUID(UUID uuid);
}
