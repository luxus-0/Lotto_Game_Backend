package pl.lotto.numberreceiver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.lotto.numberreceiver.dto.NumbersResultMessageDto;
import pl.lotto.numbersgenerator.InMemoryWinningNumbersRepository;
import pl.lotto.numbersgenerator.WinningNumbersFacade;
import pl.lotto.numbersgenerator.WinningNumbersFacadeConfiguration;
import pl.lotto.numbersgenerator.WinningNumbersRepository;
import pl.lotto.numbersgenerator.dto.WinningNumbersResultDto;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Set;
import java.util.stream.Stream;

import static java.time.LocalTime.MIDNIGHT;
import static java.time.LocalTime.NOON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static pl.lotto.numberreceiver.NumbersMessageProvider.SIZE_NUMBERS;
import static pl.lotto.numbersgenerator.WinningNumbersMessageProvider.FAILED;
import static pl.lotto.numbersgenerator.WinningNumbersMessageProvider.SUCCESS;

public class NumbersGeneratorFacadeTest {

    private final WinningNumbersRepository winningNumbersRepository;
    private final TicketDrawDate drawDate;
    private final TicketGenerator ticketGenerator;

    public NumbersGeneratorFacadeTest() {
        TicketRandomUUID uuid = new TicketRandomUUID();
        this.winningNumbersRepository = new InMemoryWinningNumbersRepository();
        TicketCurrentDateTime currentDateTime = new TicketCurrentDateTime(Clock.systemUTC());
        this.drawDate = new TicketDrawDate(currentDateTime);
        this.ticketGenerator = new TicketGenerator(uuid, drawDate);
    }

    public Ticket getTicket(LocalDateTime dateTime, Set<Integer> inputNumbers) {
        LocalDateTime ticketDrawDate = drawDate.generateDrawDate(dateTime);
        return ticketGenerator.generateTicket(inputNumbers, ticketDrawDate);
    }

    @Test
    @DisplayName("return failed when user get six numbers and result is 0 win numbers")
    public void should_return_failed_when_user_get_6_numbers_and_result_is_zero_win_numbers() {
        //given
        LocalDate date = LocalDate.of(2022, Month.OCTOBER, 29);
        LocalDateTime dateTime = LocalDateTime.of(date, NOON);
        Set<Integer> inputNumbers = Set.of(1, 2, 3, 4, 5, 6);
        Ticket ticket = getTicket(dateTime, inputNumbers);
        WinningNumbersResultDto messageTicket = new WinningNumbersResultDto(ticket, FAILED);

        WinningNumbersFacade winningNumbersFacade = new WinningNumbersFacadeConfiguration()
                .createModuleForTests(winningNumbersRepository, ticket);
        //when
        WinningNumbersResultDto winnerTicket = winningNumbersFacade.checkWinnerNumbers(ticket);
        //then
        assertThat(winnerTicket).isEqualTo(messageTicket);
        assertEquals(SIZE_NUMBERS, inputNumbers.size());
    }

    @Test
    @DisplayName("return failed when user get six numbers and result is 0 win numbers")
    public void should_return_failed_when_user_get_more_than_six_numbers_and_result_is_zero_win_numbers() {
        //given
        LocalDate date = LocalDate.of(2022, Month.OCTOBER, 29);
        LocalDateTime dateTime = LocalDateTime.of(date, NOON);
        Set<Integer> inputNumbers = Set.of(1, 2, 3, 4, 5, 6, 7);
        Ticket ticket = getTicket(dateTime, inputNumbers);
        WinningNumbersResultDto messageTicket = new WinningNumbersResultDto(ticket, FAILED);

        WinningNumbersFacade winningNumbersFacade = new WinningNumbersFacadeConfiguration()
                .createModuleForTests(winningNumbersRepository, ticket);
        //when
        WinningNumbersResultDto winnerTicket = winningNumbersFacade.checkWinnerNumbers(ticket);
        //then
        assertThat(winnerTicket).isEqualTo(messageTicket);
        assertNotEquals(SIZE_NUMBERS, inputNumbers.size());
    }

    @Test
    @DisplayName("return failed when user get six numbers and result is 0 win numbers")
    public void should_return_failed_when_user_get_less_than_six_numbers_and_result_is_zero_win_numbers() {
        //given
        LocalDate date = LocalDate.of(2022, Month.OCTOBER, 29);
        LocalDateTime dateTime = LocalDateTime.of(date, NOON);
        Set<Integer> inputNumbers = Set.of(20, 30, 40, 50, 60);
        Set<Integer> randomNumbers = Set.of(34, 75, 11, 45, 99);
        Integer randomNumber = randomNumbers.stream().findAny().orElseThrow();
        Ticket ticket = getTicket(dateTime, inputNumbers);

        WinningNumbersFacade winningNumbersFacade = new WinningNumbersFacadeConfiguration()
                .createModuleForTests(winningNumbersRepository, ticket);
        //when
        WinningNumbersResultDto result = winningNumbersFacade.checkWinnerNumbers(ticket);
        boolean isWinnerNumber = inputNumbers.contains(randomNumber);
        //then
        assertFalse(isWinnerNumber, result.messageInfo());
        assertNotEquals(SIZE_NUMBERS, inputNumbers.size());
    }

    @Test
    @DisplayName("return failed when user get six numbers and not correct date with time draw")
    public void should_return_failed_when_user_get_6_numbers_and_not_correct_date_with_time_draw() {
        //given
        Set<Integer> inputNumbers = Set.of(10, 20, 34, 40, 75, 60);
        Set<Integer> randomNumbers = Set.of(10, 20, 30, 40, 50, 60);
        LocalDate notCorrectDate = LocalDate.of(2022, Month.OCTOBER, 31);
        LocalDateTime notCorrectDateTime = LocalDateTime.of(notCorrectDate, MIDNIGHT);
        LocalDate correctDate = LocalDate.of(2022, Month.OCTOBER, 29);
        LocalDateTime correctDateTime = LocalDateTime.of(correctDate, NOON);
        Integer randomNumber = randomNumbers.stream().findAny().orElse(0);
        Ticket ticket = getTicket(correctDateTime, inputNumbers);

        WinningNumbersFacade winningNumbersFacade = new WinningNumbersFacadeConfiguration()
                .createModuleForTests(winningNumbersRepository, ticket);
        //when
        boolean isWinnerNumbers = inputNumbers.contains(randomNumber);
        //then
        assertThat(isWinnerNumbers).isEqualTo(true);
        assertNotEquals(correctDateTime, notCorrectDateTime);
        assertEquals(SIZE_NUMBERS, randomNumbers.size());
        assertEquals(SIZE_NUMBERS, inputNumbers.size());
    }

    @Test
    @DisplayName("return success when user get six numbers and result win numbers")
    public void return_success_when_user_get_six_numbers_and_result_win_numbers() {
        //given
        LocalDate date = LocalDate.of(2022, Month.OCTOBER, 29);
        LocalDateTime dateTime = LocalDateTime.of(date, NOON);
        Set<Integer> inputNumbers = Set.of(12, 20, 30, 25, 50, 77);
        Set<Integer> randomNumbers = Set.of(12, 20, 14, 17, 52, 77);
        Integer randomNumber = randomNumbers.stream().findAny().orElseThrow();
        Ticket ticket = getTicket(dateTime, inputNumbers);

        WinningNumbersFacade winningNumbersFacade = new WinningNumbersFacadeConfiguration()
                .createModuleForTests(winningNumbersRepository, ticket);
        //when
        WinningNumbersResultDto winningNumbersResult = winningNumbersFacade.checkWinnerNumbers(ticket);
        Set<Integer> winningNumbers = winningNumbersResult.ticket().numbersUser();
        boolean isWinnerNumbers = winningNumbers.contains(randomNumber);
        //then
        assertThat(isWinnerNumbers).isEqualTo(true);
        assertEquals(SIZE_NUMBERS, inputNumbers.size());
        assertEquals(SIZE_NUMBERS, randomNumbers.size());
    }
}
