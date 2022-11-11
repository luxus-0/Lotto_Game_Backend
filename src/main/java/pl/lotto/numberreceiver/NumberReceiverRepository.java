package pl.lotto.numberreceiver;

import java.util.Set;
import java.util.UUID;

public interface NumberReceiverRepository {
    NumberReceiver save(NumberReceiver numberReceiver);
    Set<Integer> findByDate(String dateTime);
    Set<Integer> findByUUID(UUID uuid);
}
