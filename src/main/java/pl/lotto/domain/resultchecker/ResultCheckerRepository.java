package pl.lotto.domain.resultchecker;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.lotto.domain.winningnumbers.WinningTicket;

import java.util.Optional;

@Repository
public interface ResultCheckerRepository extends MongoRepository<WinningTicket, String> {
    Optional<WinningTicket> findAllByTicketUUID(String ticketUUID);
}
