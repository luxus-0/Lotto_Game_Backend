package pl.lotto.domain.numbersgenerator;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface WinningNumbersRepository extends MongoRepository<WinningNumbers, String> {
    Optional<WinningNumbers> findWinningNumbersByDrawDate(LocalDateTime drawDate);
    Optional<WinningNumbers> findWinningNumbersByHash(String hash);

    WinningNumbers create(WinningNumbers winningNumbers);
}