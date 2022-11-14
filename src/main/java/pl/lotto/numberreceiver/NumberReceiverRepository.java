package pl.lotto.numberreceiver;

import java.time.LocalDateTime;
import java.util.UUID;

public interface NumberReceiverRepository {
    UserNumbers save(UserNumbers numberReceiver);

    UserNumbers findByDate(LocalDateTime dateTime);

    UserNumbers findByUUID(UUID uuid);
}
