package pl.lotto.resultannouncer.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record ResultAnnouncerDto(UUID uuid, Set<Integer> numbersUser, LocalDateTime dateTime, String message) {
}
