package pl.lotto.resultchecker;

import java.time.LocalDateTime;
import java.util.Set;

record ResultsLotto(String uuid, Set<Integer> numbersUser, Set<Integer> lottoNumbers, LocalDateTime drawDate, String message) {
}
