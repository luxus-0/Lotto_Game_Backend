package pl.lotto.numbersgenerator;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface NumbersGeneratorRepository extends MongoRepository<NumbersGenerator, UUID> {
}
