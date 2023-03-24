package pl.lotto.domain.numbersgenerator;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface RandomNumberRepository extends MongoRepository<RandomNumber, String> {
}
