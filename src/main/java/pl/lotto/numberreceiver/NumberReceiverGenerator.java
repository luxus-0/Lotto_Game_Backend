package pl.lotto.numberreceiver;

import java.util.Set;
import java.util.UUID;

class NumberReceiverGenerator {

    public NumberReceiver generateTicket(UUID uuid, Set<Integer> numbersUser, boolean isCorrectDateTimeDraw) {
        return NumberReceiver.builder()
                .uuid(uuid)
                .numbersFromUser(numbersUser)
                .isCorrectDateTimeDraw(isCorrectDateTimeDraw)
                .build();
    }
}
