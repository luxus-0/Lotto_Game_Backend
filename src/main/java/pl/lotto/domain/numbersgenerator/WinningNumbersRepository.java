package pl.lotto.domain.numbersgenerator;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface WinningNumbersRepository extends MongoRepository<WinningTicket, String> {
    Optional<WinningTicket> findWinningNumbersByDrawDate(LocalDateTime drawDate);
    boolean existsByDrawDate(LocalDateTime drawDate);
}
