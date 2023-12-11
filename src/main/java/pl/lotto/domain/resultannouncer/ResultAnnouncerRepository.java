package pl.lotto.domain.resultannouncer;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ResultAnnouncerRepository extends MongoRepository<ResultAnnouncerResponse, String> {
    Optional<ResultAnnouncerResponse> findAllByTicketUUID(String ticketUUID);
}
