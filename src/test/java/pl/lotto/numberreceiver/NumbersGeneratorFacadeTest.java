package pl.lotto.numberreceiver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

import static java.time.LocalTime.NOON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.lotto.numbersgenerator.WinningNumbersMessageProvider.FAILED;

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
    }

    @Test
    @DisplayName("return success when user get six numbers and result is win numbers")
    public void return_success_when_user_get_six_numbers_and_result_is_zero_win_numbers() {
        //given
        LocalDate date = LocalDate.of(2022, Month.OCTOBER, 29);
        LocalDateTime dateTime = LocalDateTime.of(date, NOON);
        Set<Integer> inputNumbers = Set.of(12, 20, 30, 25, 50, 77);
        Set<Integer> randomNumbers = Set.of(12, 20, 11, 17, 52, 77);
        int randomNumber = randomNumbers.stream().findAny().orElseThrow();
        Ticket ticket = getTicket(dateTime, inputNumbers);

        WinningNumbersFacade winningNumbersFacade = new WinningNumbersFacadeConfiguration()
                .createModuleForTests(winningNumbersRepository, ticket);
        //when
        WinningNumbersResultDto isWinnerNumbers = winningNumbersFacade.checkWinnerNumbers(ticket);
        //then
        Set<Integer> winnerNumbers = isWinnerNumbers.ticket().numbersUser();
        assertTrue(winnerNumbers.contains(randomNumber));
    }
}
