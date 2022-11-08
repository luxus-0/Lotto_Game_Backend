package pl.lotto.resultchecker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.lotto.resultchecker.dto.ResultsLotto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResultsCheckerFacadeTest {

    @Test
    @DisplayName("return success when user get 6 numbers and is winner numbers")
    public void should_return_success_when_user_get_six_numbers_and_is_winner_numbers() {
        //given
        Set<Integer> inputNumbers = Set.of(12, 75, 11, 19, 45, 78);
        Set<Integer> lottoNumbers = Set.of(11, 75, 78, 90, 14, 45);
        UUID uuid = UUID.randomUUID();
        LocalDateTime drawDateTime = LocalDateTime.of(2001, 12,7, 12, 0);
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .createModuleForTests();
        ResultsLotto allResults = new ResultsLotto(uuid, inputNumbers, lottoNumbers, drawDateTime, ResultsCheckerMessageProvider.NOT_WIN);

        //when
        ResultsLotto result = resultsCheckerFacade.getAllResults(allResults);

        //then
        assertEquals(allResults, result);
    }
}