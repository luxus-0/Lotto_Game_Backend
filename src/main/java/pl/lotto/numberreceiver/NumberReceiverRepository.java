package pl.lotto.numberreceiver;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface NumberReceiverRepository extends MongoRepository<UserNumbers, UUID> {
    <S extends UserNumbers> S save(S entity);
    UserNumbers findByDate(LocalDateTime dateTime);
    UserNumbers findByUUID(UUID uuid);
    void deleteByUUID(UUID uuid);
    void delete();
}
