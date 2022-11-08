package pl.lotto.numberreceiver;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

class NumberReceiverGenerator {

    public NumberReceiver generateTicket(UUID uuid, Set<Integer> numbersUser, LocalDateTime drawDate) {
        return NumberReceiver.builder()
                .uuid(uuid)
                .numbersFromUser(numbersUser)
                .drawDate(drawDate)
                .build();
    }
}
