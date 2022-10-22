package pl.lotto.numberreceiver;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

class Client {
    private UUID hash;
    private LocalDateTime dateOfTheDraw;
    private Set<Integer> numbers;
}
