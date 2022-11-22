package pl.lotto.resultchecker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.lotto.resultchecker.dto.ResultsLottoDto;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;

import static java.time.Month.DECEMBER;
import static java.time.Month.SEPTEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static pl.lotto.resultchecker.ResultsCheckerMessageProvider.NOT_WIN;
import static pl.lotto.resultchecker.ResultsCheckerMessageProvider.WIN;

class ResultsCheckerFacadeTest {

    Clock clock = Clock.fixed(Instant.now(Clock.systemUTC()), ZoneId.systemDefault());
    ResultCheckerDateTime resultCheckerDateTime = new ResultCheckerDateTime(clock);

    @Test
    @DisplayName("return success when user get 6 numbers and is winner numbers")
    public void should_return_success_when_user_get_six_numbers_and_is_winner_numbers() {
        //given
        Set<Integer> inputNumbers = Set.of(12, 75, 11, 19, 45, 78);
        LocalDateTime datetimeDraw = LocalDateTime.of(2022, DECEMBER, 10, 12, 0);
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .createModuleForTests(clock);
        //when
        ResultsLottoDto resultsLotto = resultsCheckerFacade.getWinnerNumbers(inputNumbers, datetimeDraw);

        //then
        assertNotEquals(resultsLotto.message(), WIN);
    }

    @Test
    @DisplayName("return failed when user get 6 numbers and is not winner numbers")
    public void should_return_failed_when_user_get_six_numbers_and_is_not_winner_numbers() {
        //given
        Set<Integer> inputNumbers = Set.of(12, 75, 11, 19, 45, 78);
        LocalDateTime datetimeDraw = LocalDateTime.of(2022, DECEMBER, 3, 12, 0);
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .createModuleForTests(clock);

        //when
        ResultsLottoDto resultWinner = resultsCheckerFacade.getWinnerNumbers(inputNumbers, datetimeDraw);
        //then
        assertEquals(resultWinner.message(), NOT_WIN);
    }

    @Test
    @DisplayName("return failed when user get 5 numbers and is not winner numbers")
    public void should_return_failed_when_user_get_five_numbers_and_is_winner_numbers() {
        //given
        Set<Integer> inputNumbers = Set.of(12, 75, 11, 19, 45);
        LocalDateTime datetimeDraw = LocalDateTime.of(2022, DECEMBER, 3, 12, 0);
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .createModuleForTests(clock);

        //when
        ResultsLottoDto resultsWinner = resultsCheckerFacade.getWinnerNumbers(inputNumbers, datetimeDraw);
        //then
        assertThat(resultsWinner.message()).isEqualTo(NOT_WIN);
    }

    @Test
    @DisplayName("return failed when user get more than 6 numbers and is not winner numbers")
    public void should_return_failed_when_user_get_more_than_six_numbers_and_is_not_winner_numbers() {
        //given
        LocalDateTime datetimeDraw = LocalDateTime.of(2022, DECEMBER, 3, 12, 0);
        Set<Integer> inputNumbers = Set.of(12, 75, 11, 19, 45, 88, 31);

        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .createModuleForTests(clock);

        //when
        String resultMessageWinner = resultsCheckerFacade.getWinnerNumbers(inputNumbers, datetimeDraw).message();
        //then
        assertThat(resultMessageWinner).isNotEqualTo(WIN);
    }

    @Test
    @DisplayName("return failed when user get less than six numbers and is not winner numbers")
    public void should_return_failed_when_user_get_less_than_six_numbers_and_is_not_winner_numbers() {
        //given
        Set<Integer> inputNumbers = Set.of(1, 2, 3);
        LocalDateTime datetimeDraw = LocalDateTime.of(2022, DECEMBER, 3, 12, 0);
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .createModuleForTests(clock);

        //when
        ResultsLottoDto resultsLotto = resultsCheckerFacade.getWinnerNumbers(inputNumbers, datetimeDraw);

        //then
        assertEquals(resultsLotto.message(), NOT_WIN);
    }

    @Test
    @DisplayName("return failed date time draw when user get numbers with date time")
    public void should_return_failed_date_time_draw_when_user_get_numbers_with_date_time() {
        //given
        Set<Integer> inputNumbers = Set.of(1, 2, 3, 4, 5, 6);
        LocalDateTime datetimeDraw = LocalDateTime.of(2022, DECEMBER, 3, 12, 0);

        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .createModuleForTests(clock);

        //when
        LocalDateTime resultDateTime = resultsCheckerFacade.getWinnerNumbers(inputNumbers, datetimeDraw).dateTimeDraw();
        //then
       LocalDateTime expectedDateTime = LocalDateTime.of(2022, SEPTEMBER, 7, 12, 0);
       assertNotEquals(resultDateTime, expectedDateTime);
    }

    @Test
    @DisplayName("return not win message when user get no_winner numbers")
    public void should_return_not_win_message_when_user_get_no_winner_numbers() {
        //given
        Set<Integer> inputNumbers = Set.of(25, 78, 94, 11, 34, 45);
        LocalDateTime datetimeDraw = LocalDateTime.of(2022, DECEMBER, 10, 12, 0);
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .createModuleForTests(clock);

        //when
        String actualWinnersMessage = resultsCheckerFacade.getWinnerNumbers(inputNumbers, datetimeDraw).message();
        //then
        assertThat(actualWinnersMessage).isEqualTo("NOT WIN");
    }

    @Test
    @DisplayName("return success when user get more than 1 winner numbers")
    public void should_return_success_message_when_user_get_winner_numbers() {
        //given
        Set<Integer> inputNumbers = Set.of(45, 78, 94, 11, 34, 90);
        LocalDateTime datetimeDraw = LocalDateTime.of(2022, DECEMBER, 10, 12, 0);
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .createModuleForTests(clock);

        //when
        Set<Integer> resultWinnerNumbers = resultsCheckerFacade.getWinnerNumbers(inputNumbers, datetimeDraw).winnerNumbers();
        //then
        boolean checkEmptyWinnerNumbers = resultWinnerNumbers.isEmpty();

        assertThat(checkEmptyWinnerNumbers).isFalse();
    }

    @Test
    @DisplayName("return failed when user get no winner numbers")
    public void should_return_failed_message_when_user_get_no_winner_numbers() {
        //given
        Set<Integer> inputNumbers = Set.of(45, 78, 94, 11, 34, 90);
        LocalDateTime datetimeDraw = LocalDateTime.of(2022, DECEMBER, 10, 12, 0);
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .createModuleForTests(clock);

        //when
        Set<Integer> resultWinnerNumbers = resultsCheckerFacade.getWinnerNumbers(inputNumbers, datetimeDraw).winnerNumbers();
        //then
        assertThat(resultWinnerNumbers.isEmpty()).isEqualTo(false);
    }

    @Test
    @DisplayName("return failed date time draw when user get incorrect date time")
    public void should_return_failed_date_time_draw_when_user_get_incorrect_date_time() {
        //given
        Set<Integer> inputNumbers = Set.of(45, 78, 94, 11, 34, 90);
        LocalDateTime datetimeDraw = LocalDateTime.of(2022, DECEMBER, 15, 12, 0);

        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .createModuleForTests(clock);

        //when
        LocalDateTime resultWinnerDateTime = resultsCheckerFacade.getWinnerNumbers(inputNumbers, datetimeDraw).dateTimeDraw();
        //then
        LocalDateTime nextSaturdayAt12am = resultCheckerDateTime.readDateTimeDraw();

        assertNotEquals(resultWinnerDateTime, nextSaturdayAt12am);
    }
}