package pl.lotto.numberreceiver;

import java.time.LocalDateTime;
import java.util.Set;

public interface NumberReceiverRepository {
    Set<Integer> save(NumberReceiver numberReceiver);

    Set<Integer> findByDate(LocalDateTime drawDate);


    Set<Integer> findByUUID(String uuid);
}
