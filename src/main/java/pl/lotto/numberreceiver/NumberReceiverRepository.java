package pl.lotto.numberreceiver;

import org.springframework.data.repository.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface NumberReceiverRepository extends Repository<UserNumbers, UUID> {
    <S extends UserNumbers> S save(S entity);

    LocalDateTime findByDate(LocalDateTime dateTime);

    UserNumbers findByUUID(UUID uuid);
}
