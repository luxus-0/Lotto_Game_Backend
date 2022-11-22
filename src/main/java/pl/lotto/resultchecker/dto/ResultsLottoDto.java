package pl.lotto.resultchecker.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record ResultsLottoDto(Set<Integer> winnerNumbers, LocalDateTime dateTimeDraw, String message) {
}
