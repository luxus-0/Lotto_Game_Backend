package pl.lotto.numberreceiver;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface NumberReceiverRepository {
    <S extends UserNumbers> S save(S entity);
    UserNumbers findByDate(LocalDateTime dateTime);
    UserNumbers findByUUID(UUID uuid);
}
