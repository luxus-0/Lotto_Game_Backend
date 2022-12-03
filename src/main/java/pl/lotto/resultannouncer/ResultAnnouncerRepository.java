package pl.lotto.resultannouncer;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ResultAnnouncerRepository extends MongoRepository<ResultAnnouncer, UUID> {
    @Query("SELECT * FROM ResultAnnouncer WHERE result = :result")
    List<ResultAnnouncer> findByResult(String result);
}
