package pl.lotto.numberreceiver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.lotto.numberreceiver.dto.NumbersResultMessageDto;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Set;

import static java.time.LocalTime.NOON;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.lotto.numberreceiver.NumbersMessageProvider.*;

class NumberReceiverFacadeTest {

    private final TicketRepository ticketRepository;
    private final TicketDrawDate ticketDrawDate;

    public NumberReceiverFacadeTest() {
        this.ticketRepository = new InMemoryTicketRepository();
        this.ticketDrawDate = new TicketDrawDate(Clock.systemUTC());
    }

    @Test
    @DisplayName("return success when user gave six numbers and day is saturday and time is noon")
    public void should_return_success_when_user_gave_six_numbers_and_day_is_saturday_and_time_is_noon() {
        // given
        LocalDate date = LocalDate.of(2022, Month.OCTOBER, 29);
        LocalDateTime dateTime = LocalDateTime.of(date, NOON);
        LocalDateTime drawDate = ticketDrawDate.generateDrawDate(dateTime);
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration()
                .createModuleForTests(ticketRepository, drawDate);
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 6);
        // when
        NumbersResultMessageDto inputNumbers = numberReceiverFacade.inputNumbers(numbers);
        // then
        List<String> messages = List.of(EQUALS_SIX_NUMBERS);
        NumbersResultMessageDto result = new NumbersResultMessageDto(numbers, messages);
        assertThat(inputNumbers).isEqualTo(result);
    }

    @Test
    @DisplayName("return true when user gave six numbers and day is saturday and time is noon")
    public void should_return_true_when_user_gave_six_numbers_and_day_is_saturday_and_time_is_noon() {
        // given
        LocalDate date = LocalDate.of(2022, Month.NOVEMBER, 5);
        LocalDateTime dateTime = LocalDateTime.of(date, NOON);
        LocalDateTime drawDate = ticketDrawDate.generateDrawDate(dateTime);
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration()
                .createModuleForTests(ticketRepository, drawDate);
        Set<Integer> numbers = Set.of(90, 1, 2, 3, 4, 19);
        // when
        NumbersResultMessageDto inputNumbersForUser = numberReceiverFacade.inputNumbers(numbers);
        // then
        NumbersResultMessageDto resultMessage = new NumbersResultMessageDto(numbers, List.of(EQUALS_SIX_NUMBERS));
        assertThat(inputNumbersForUser).isEqualTo(resultMessage);
    }

    @Test
    @DisplayName("return failed when user gave less than six numbers")
    public void should_return_failed_when_user_gave_less_than_six_numbers() {
        // given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration()
                .createModuleForTests(ticketRepository, null);
        Set<Integer> numbers = Set.of(1, 2, 3, 4);
        // when
        NumbersResultMessageDto inputNumbers = numberReceiverFacade.inputNumbers(numbers);
        // then
        List<String> messages = List.of(LESS_THAN_SIX_NUMBERS);
        NumbersResultMessageDto result = new NumbersResultMessageDto(numbers, messages);
        assertThat(inputNumbers).isEqualTo(result);
    }

    @Test
    @DisplayName("return failed when user gave more than six numbers")
    public void should_return_failed_when_user_gave_more_than_six_numbers() {
        // given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration()
                .createModuleForTests(ticketRepository, null);
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 6, 12, 14);
        // when
        NumbersResultMessageDto inputNumbers = numberReceiverFacade.inputNumbers(numbers);
        // then
        List<String> messages = List.of(MORE_THAN_SIX_NUMBERS);
        NumbersResultMessageDto result = new NumbersResultMessageDto(numbers, messages);
        assertThat(inputNumbers).isEqualTo(result);
    }

    @Test
    @DisplayName("return failed when user gave number out of range")
    public void should_return_failed_when_user_gave_number_out_of_range() {
        // given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration()
                .createModuleForTests(ticketRepository, null);
        Set<Integer> numbers = Set.of(100, 1, 2, 3, 4, -3);
        // when
        NumbersResultMessageDto inputNumbers = numberReceiverFacade.inputNumbers(numbers);
        // then
        List<String> messages = List.of(EQUALS_SIX_NUMBERS);
        NumbersResultMessageDto result = new NumbersResultMessageDto(numbers, messages);
        assertThat(inputNumbers).isEqualTo(result);
    }
}
