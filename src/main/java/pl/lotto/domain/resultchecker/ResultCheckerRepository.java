package pl.lotto.domain.resultchecker;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResultCheckerRepository extends MongoRepository<TicketResults, String> {
    Optional<TicketResults> findAllByTicketUUID(String ticketUUID);
}
