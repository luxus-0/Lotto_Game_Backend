package pl.lotto.domain.resultchecker;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryPlayerDatabaseImpl implements PlayerRepository {

    private final Map<String, Player> inMemoryPlayers = new ConcurrentHashMap<>();

    @Override
    public List<Player> saveAll(List<Player> players) {
        players.forEach(player -> inMemoryPlayers.put(player.hash(), player));
        return players;
    }

    @Override
    public Player save(Player player) {
        return inMemoryPlayers.put(player.hash(), player);
    }

    @Override
    public Optional<Player> findById(String hash) {
        return Optional.ofNullable(inMemoryPlayers.get(hash));
    }
}
