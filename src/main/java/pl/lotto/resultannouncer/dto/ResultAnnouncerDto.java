package pl.lotto.resultannouncer.dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record ResultAnnouncerDto(UUID uuid, Set<Integer> winnerNumbers, LocalDateTime dateTimeWinner, boolean checkWinnerNumbers) {
}
