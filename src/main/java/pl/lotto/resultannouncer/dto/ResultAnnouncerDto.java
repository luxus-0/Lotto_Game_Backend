package pl.lotto.resultannouncer.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record ResultAnnouncerDto(Set<Integer> resultNumbers, LocalDateTime resultDateTime, String resultForUser) {
}
