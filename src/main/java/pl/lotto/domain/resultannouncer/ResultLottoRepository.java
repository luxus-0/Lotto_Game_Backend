package pl.lotto.domain.resultannouncer;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResultLottoRepository extends MongoRepository<ResultLotto, String> {
}
