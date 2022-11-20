package pl.lotto.resultchecker;

import java.time.LocalDateTime;
import java.util.Set;

interface ResultsCheckerRepository {
    ResultsLotto getWinnersByDate(LocalDateTime dateTime, Set<Integer> userNumbers);

    ResultsLotto save(ResultsLotto resultsLotto);
}
