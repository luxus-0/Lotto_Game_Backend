package pl.lotto.resultchecker;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

record ResultsLotto(UUID uuid, Set<Integer> numbersUser, Set<Integer> lottoNumbers, LocalDateTime drawDate,
                    String message) {
}
