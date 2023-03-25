package pl.lotto.domain.numbersgenerator;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;

public interface WinningNumbersRepository extends MongoRepository<WinningNumbers, String> {
    WinningNumbers findWinningNumbersByDate(LocalDateTime dateTime);
}
