package pl.lotto.domain.resultannouncer.dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record ResultAnnouncerDto(UUID uuid, Set<Integer> numbersUser, LocalDateTime dateTime, String message) {
}
