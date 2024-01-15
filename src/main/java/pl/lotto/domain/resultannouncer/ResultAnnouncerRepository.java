package pl.lotto.domain.resultannouncer;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResultAnnouncerRepository extends MongoRepository<ResultAnnouncerResponse, String> {
    Optional<ResultAnnouncerResponse> findAllByTicketUUID(String ticketUUID);
}
