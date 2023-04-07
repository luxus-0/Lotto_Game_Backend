package pl.lotto.domain.resultchecker;

import java.util.List;
import java.util.Optional;

interface ResultsCheckerRepository {
    List<Player> saveAll(List<Player> players);

    Optional<Player> findById(String hash);
}
