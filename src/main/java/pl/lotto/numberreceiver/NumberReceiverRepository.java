package pl.lotto.numberreceiver;

import java.time.LocalDateTime;

public interface NumberReceiverRepository {
    <S extends UserNumbers> S save(S entity);
    LocalDateTime findDate(LocalDateTime dateTime);
}
