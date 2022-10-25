package pl.lotto.numbersgenerator.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record WinningsNumberDto(Set<Integer> userNumbers, Set<Integer> randomNumbers, LocalDateTime date) {
}
