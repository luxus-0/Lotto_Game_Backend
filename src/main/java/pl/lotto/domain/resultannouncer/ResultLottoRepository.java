package pl.lotto.domain.resultannouncer;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ResultLottoRepository extends MongoRepository<ResultLotto, String> {
    Optional<ResultLotto> findByTicketUUID(String ticketUuid);
}
