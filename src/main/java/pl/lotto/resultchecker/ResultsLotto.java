package pl.lotto.resultchecker;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
class ResultsLotto {
    private final String uuid;
    private final Set<Integer> numbersUser;
    private final Set<Integer> winningNumbers;
    private final LocalDateTime drawDate;
    private final boolean isWinner;
}
