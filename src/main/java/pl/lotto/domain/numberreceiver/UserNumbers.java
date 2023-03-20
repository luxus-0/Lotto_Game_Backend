package pl.lotto.domain.numberreceiver;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Document("user_numbers")
public record UserNumbers(UUID uuid, Set<Integer> numbersFromUser, LocalDateTime dateTimeDraw) {
}
