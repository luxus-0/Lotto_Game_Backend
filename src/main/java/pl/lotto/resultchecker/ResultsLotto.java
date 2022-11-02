package pl.lotto.resultchecker;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

record ResultsLotto(String uuid, Set<Integer> numbersUser, Set<Integer> winningNumbers, LocalDateTime drawDate,
                    boolean isWinner, String message) {
}
