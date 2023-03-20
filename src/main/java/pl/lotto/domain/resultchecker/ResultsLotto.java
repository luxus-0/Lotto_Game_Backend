package pl.lotto.domain.resultchecker;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

class ResultsLotto {
    UUID uuid;
    Set<Integer> inputNumbers;
    LocalDateTime dateTimeDraw;

    ResultsLotto(UUID uuid, Set<Integer> inputNumbers, LocalDateTime dateTimeDraw) {
        this.uuid = uuid;
        this.inputNumbers = inputNumbers;
        this.dateTimeDraw = dateTimeDraw;
    }
}
