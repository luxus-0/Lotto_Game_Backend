package pl.lotto.numbersgenerator;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NumbersGeneratorRepository extends MongoRepository<NumbersGenerator, UUID> {
}
