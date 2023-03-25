package pl.lotto.domain.numbersgenerator;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RandomNumberRepository extends MongoRepository<RandomNumber, String> {
    List<RandomNumber> findRandomNumberByUuid(String uuid);
}
