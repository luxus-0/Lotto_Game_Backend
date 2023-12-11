package pl.lotto.domain.resultchecker;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ResultCheckerRepository extends MongoRepository<ResultCheckerResponse, String> {
    Optional<ResultCheckerResponse> findAllByTicketUUID(String ticketUUID);
}
