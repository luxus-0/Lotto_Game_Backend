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

    void printWinnerNumber(String result) {
        int win = Integer.parseInt(result);
        System.out.println(win + " " + NOT_WIN);
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
    public void should_return_failed_when_user_get_more_than_six_numbers_and_is_winner_numbers() {
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
    public void should_return_failed_when_user_get_more_than_six_numbers_and_is_not_winner_numbers() {
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

    @Test
    @DisplayName("return success message when user get 6 numbers and is winner numbers")
    public void should_return_true_when_user_get_six_numbers_and_is_winner_numbers() {
        //given
        Set<Integer> inputNumbers = Set.of(12, 75, 11, 19, 45, 88);
        ResultsLotto resultsLotto = new ResultsLotto(inputNumbers, WIN);
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .createModuleForTests(validator);

        //when
        String resultMessage = resultsCheckerFacade.getWinnerNumbers(inputNumbers)
                .message();

        //then
        assertThat(resultsLotto.message()).isEqualTo(resultMessage);
    }

    @Test
    @DisplayName("return failed message when user get 6 numbers and is not winner numbers")
    public void should_return_failed_message_when_user_get_six_numbers_and_is_not_winner_numbers() {
        //given
        Set<Integer> inputNumbers = Set.of(12, 75, 11, 19, 45, 88);
        ResultsLotto resultsLotto = new ResultsLotto(inputNumbers, NOT_WIN);
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .createModuleForTests(validator);

        //when
        String resultMessage = resultsCheckerFacade.getWinnerNumbers(inputNumbers)
                .message();

        //then
        assertThat(resultsLotto.message()).isNotEqualTo(resultMessage);
    }

    @Test
    @DisplayName("return throwing exception when user get less than six numbers and is not winner numbers")
    public void should_return_throwing_exception_when_user_get_less_than_six_numbers_and_is_not_winner_numbers() {
        //given
        Set<Integer> inputNumbers = Set.of(1, 2, 3);
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .createModuleForTests(validator);

        //when
        String result = resultsCheckerFacade.getWinnerNumbers(inputNumbers).message();

        //then
        ResultsLotto resultsLotto = new ResultsLotto(inputNumbers, result);
        assertThrows(RuntimeException.class, () -> printWinnerNumber(result));

        String expectedMessage = "not win";
        String actualMessage = resultsLotto.message();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}