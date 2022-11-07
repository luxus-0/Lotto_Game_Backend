package pl.lotto.numberreceiver;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public interface NumberReceiverRepository {
    Set<Integer> save(NumberReceiver numberReceiver);

    Set<Integer> findByDate(LocalDateTime drawDate);

    Set<Integer> findByUUID(UUID uuid);
}
