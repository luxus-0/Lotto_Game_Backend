package pl.lotto.numberreceiver;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface DatabaseNumberReceiverRepository extends MongoRepository<UserNumbers, UUID> {
    UserNumbers findUserByDateTime(LocalDateTime dateTime);
    UserNumbers findUserByUUID(UUID uuid);
}
