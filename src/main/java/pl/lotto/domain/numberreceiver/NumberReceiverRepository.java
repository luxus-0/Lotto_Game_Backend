package pl.lotto.domain.numberreceiver;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface NumberReceiverRepository extends MongoRepository<UserNumbers, UUID> {
}
