package pl.lotto.resultchecker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.lotto.resultchecker.dto.ResultsLottoDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;

import static java.time.Month.DECEMBER;
import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.*;
import static pl.lotto.resultchecker.ResultsCheckerMessageProvider.NOT_WIN;
import static pl.lotto.resultchecker.ResultsCheckerMessageProvider.WIN;

class ResultsCheckerFacadeTest {

    @Test
    @DisplayName("return success when user get 6 numbers and is winner numbers")
    public void should_return_success_when_user_get_six_numbers_and_is_winner_numbers() {
        //given
        LocalDateTime datetimeDraw = LocalDateTime.of(2022, DECEMBER, 3, 12, 0);
        Clock clock = Clock.fixed(datetimeDraw.toInstant(UTC), ZoneId.systemDefault());
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .createModuleForTests(clock);

        Set<Integer> inputNumbers = Set.of(12, 75, 11, 19, 45, 78);
        ResultsLottoDto winnerResultLotto = new ResultsLottoDto(inputNumbers, WIN);
        //when
        ResultsLottoDto resultsLotto = resultsCheckerFacade.getWinnerNumbers(inputNumbers);

        //then
        assertNotEquals(winnerResultLotto.message(), resultsLotto.message());
    }

    @Test
    @DisplayName("return failed when user get 6 numbers and is not winner numbers")
    public void should_return_failed_when_user_get_six_numbers_and_is_not_winner_numbers() {
        //given
        LocalDateTime datetimeDraw = LocalDateTime.of(2022, DECEMBER, 3, 12, 0);
        Clock clock = Clock.fixed(datetimeDraw.toInstant(UTC), ZoneId.systemDefault());
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .createModuleForTests(clock);

        Set<Integer> inputNumbers = Set.of(12, 75, 11, 19, 45, 78);
        ResultsLottoDto notWinner = new ResultsLottoDto(inputNumbers, NOT_WIN);

        //when
        ResultsLottoDto result = resultsCheckerFacade.getWinnerNumbers(inputNumbers);
        //then
        assertEquals(notWinner, result);
    }

    @Test
    @DisplayName("return failed when user get 5 numbers and is not winner numbers")
    public void should_return_failed_when_user_get_five_numbers_and_is_winner_numbers() {
        //given
        LocalDateTime datetimeDraw = LocalDateTime.of(2022, DECEMBER, 3, 12, 0);
        Clock clock = Clock.fixed(datetimeDraw.toInstant(UTC), ZoneId.systemDefault());
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .createModuleForTests(clock);

        Set<Integer> inputNumbers = Set.of(12, 75, 11, 19, 45);
        ResultsLottoDto winner = new ResultsLottoDto(inputNumbers, WIN);
        //when
        ResultsLottoDto resultsLotto = resultsCheckerFacade.getWinnerNumbers(inputNumbers);
        //then
        assertNotEquals(winner, resultsLotto);
    }

    @Test
    @DisplayName("return failed when user get more than 6 numbers and is not winner numbers")
    public void should_return_failed_when_user_get_more_than_six_numbers_and_is_not_winner_numbers() {
        //given
        LocalDateTime datetimeDraw = LocalDateTime.of(2022, DECEMBER, 3, 12, 0);
        Clock clock = Clock.fixed(datetimeDraw.toInstant(UTC), ZoneId.systemDefault());
        Set<Integer> inputNumbers = Set.of(12, 75, 11, 19, 45, 88, 31);

        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .createModuleForTests(clock);

        //when
        ResultsLottoDto result = resultsCheckerFacade.getWinnerNumbers(inputNumbers);
        //then
        ResultsLottoDto resultWinner = new ResultsLottoDto(inputNumbers, WIN);

        assertNotEquals(resultWinner.message(), result.message());
    }

    @Test
    @DisplayName("return failed when user get less than six numbers and is not winner numbers")
    public void should_return_failed_when_user_get_less_than_six_numbers_and_is_not_winner_numbers() {
        //given
        Set<Integer> inputNumbers = Set.of(1, 2, 3);
        LocalDateTime datetimeDraw = LocalDateTime.of(2022, DECEMBER, 3, 12, 0);
        Clock clock = Clock.fixed(datetimeDraw.toInstant(UTC), ZoneId.systemDefault());
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .createModuleForTests(clock);

        //when
        ResultsLottoDto resultsLotto = resultsCheckerFacade.getWinnerNumbers(inputNumbers);

        //then
        assertEquals(resultsLotto.message(), NOT_WIN);
    }
}