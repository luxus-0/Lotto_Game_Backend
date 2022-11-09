package pl.lotto.numberreceiver;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public interface NumberReceiverRepository {
    Set<Integer> save(UUID uuid, Set<Integer> numbersFromUser);
    Set<Integer> save(LocalDateTime dateTime, Set<Integer> numbersFromUser);
    Set<Integer> findByDate(String dateTime);
    Set<Integer> findByUUID(UUID uuid);
}
