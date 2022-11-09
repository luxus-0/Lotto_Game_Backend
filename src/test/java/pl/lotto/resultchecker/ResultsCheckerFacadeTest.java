package pl.lotto.resultchecker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.lotto.resultchecker.dto.ResultsLotto;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static pl.lotto.resultchecker.ResultsCheckerMessageProvider.NOT_WIN;
import static pl.lotto.resultchecker.ResultsCheckerMessageProvider.WIN;

class ResultsCheckerFacadeTest {

    private final ResultsCheckerValidator validator;

    ResultsCheckerFacadeTest() {
        this.validator = new ResultsCheckerValidator();
    }

    @Test
    @DisplayName("return success when user get 6 numbers and is winner numbers")
    public void should_return_success_when_user_get_six_numbers_and_is_winner_numbers() {
        //given
        Set<Integer> inputNumbers = Set.of(12, 75, 11, 19, 45, 78);
        ResultsLotto winner = new ResultsLotto(inputNumbers, WIN);
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .createModuleForTests(validator);

        //when
        ResultsLotto result = resultsCheckerFacade.getWinnerNumbers(winner.resultNumbers());

        //then
        assertEquals(winner, result);
    }

    @Test
    @DisplayName("return failed when user get 6 numbers and is not winner numbers")
    public void should_return_success_when_user_get_six_numbers_and_is_not_winner_numbers() {
        //given
        Set<Integer> inputNumbers = Set.of(12, 75, 11, 19, 45, 78);
        ResultsLotto notWinner = new ResultsLotto(inputNumbers, NOT_WIN);
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .createModuleForTests(validator);

        //when
        ResultsLotto results = new ResultsLotto(inputNumbers, WIN);
        ResultsLotto result = resultsCheckerFacade.getWinnerNumbers(results.resultNumbers());

        //then
        assertNotEquals(notWinner, result);
    }

    @Test
    @DisplayName("return failed when user get 5 numbers and is winner numbers")
    public void should_return_failed_when_user_get_five_numbers_and_is_winner_numbers() {
        //given
        Set<Integer> inputNumbers = Set.of(12, 75, 11, 19, 45);
        ResultsLotto notWinner = new ResultsLotto(inputNumbers, WIN);
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .createModuleForTests(validator);

        //when
        ResultsLotto results = new ResultsLotto(inputNumbers, WIN);
        ResultsLotto result = resultsCheckerFacade.getWinnerNumbers(results.resultNumbers());
        //then
        assertNotEquals(notWinner, result);
    }

    @Test
    @DisplayName("return failed when user get more than 6 numbers and is winner numbers")
    public void should_return_failed_when_user_get_more_than_6_numbers_and_is_winner_numbers() {
        //given
        Set<Integer> inputNumbers = Set.of(12, 75, 11, 19, 45, 88, 23);
        ResultsLotto win = new ResultsLotto(inputNumbers, WIN);
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .createModuleForTests(validator);

        //when
        ResultsLotto results = new ResultsLotto(inputNumbers, WIN);
        ResultsLotto result = resultsCheckerFacade.getWinnerNumbers(results.resultNumbers());
        //then
        assertNotEquals(win, result);
    }

    @Test
    @DisplayName("return failed when user get more than 6 numbers and is not winner numbers")
    public void should_return_failed_when_user_get_more_than_6_numbers_and_is_not_winner_numbers() {
        //given
        Set<Integer> inputNumbers = Set.of(12, 75, 11, 19, 45, 88, 31);
        ResultsLotto notWin = new ResultsLotto(inputNumbers, NOT_WIN);
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .createModuleForTests(validator);

        //when
        ResultsLotto results = new ResultsLotto(inputNumbers, WIN);
        ResultsLotto result = resultsCheckerFacade.getWinnerNumbers(results.resultNumbers());
        //then
        assertThat(notWin).isEqualTo(result);
    }
}