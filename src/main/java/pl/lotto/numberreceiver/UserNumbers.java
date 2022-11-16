package pl.lotto.numberreceiver;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

class UserNumbers {
    UUID uuid;
    Set<Integer> numbersFromUser;
    LocalDateTime dateTimeDraw;

    UserNumbers(UUID uuid, Set<Integer> numbersFromUser, LocalDateTime dateTimeDraw) {
        this.uuid = uuid;
        this.numbersFromUser = numbersFromUser;
        this.dateTimeDraw = dateTimeDraw;
    }
}
