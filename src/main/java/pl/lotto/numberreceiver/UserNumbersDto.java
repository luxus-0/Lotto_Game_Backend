package pl.lotto.numberreceiver;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

record UserNumbersDto(UUID uuid, Set<Integer> numbersByDate, LocalDateTime date) {
    }
