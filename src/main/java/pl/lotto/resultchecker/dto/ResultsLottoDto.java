package pl.lotto.resultchecker.dto;

import java.util.Set;

public record ResultsLottoDto(Set<Integer> winnerNumbers, java.time.LocalDateTime dateTimeDraw, String message) {
}
