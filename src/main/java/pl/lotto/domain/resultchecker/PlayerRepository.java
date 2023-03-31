package pl.lotto.domain.resultchecker;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository {
    List<Player> saveAll(List<Player> players);
    Optional<Player> findById(String hash);
}
