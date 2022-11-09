package pl.lotto.numberreceiver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class InMemoryNumberReceiverRepository implements NumberReceiverRepository {
    private final Map<UUID, Set<Integer>> uniqueNumbersMap = new HashMap<>();
    private final Map<LocalDateTime, Set<Integer>> dateTimeNumbersMap = new HashMap<>();

    @Override
    public Set<Integer> save(UUID uuid, Set<Integer> numbersFromUser) {
        return uniqueNumbersMap.put(uuid, numbersFromUser);
    }

    @Override
    public Set<Integer> save(LocalDateTime dateTime, Set<Integer> numbersFromUser) {
        return dateTimeNumbersMap.put(dateTime, numbersFromUser);
    }

    @Override
    public Set<Integer> findByDate(String dateTime) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");
        LocalDateTime drawDateTime = LocalDateTime.parse(dateTime, dateFormat);
        return dateTimeNumbersMap.get(drawDateTime);
    }

    @Override
    public Set<Integer> findByUUID(UUID uuid) {
        return uniqueNumbersMap.get(uuid);
    }
}
