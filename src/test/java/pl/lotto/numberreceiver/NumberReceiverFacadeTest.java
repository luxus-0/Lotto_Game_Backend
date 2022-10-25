package pl.lotto.numberreceiver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.lotto.numberreceiver.dto.NumbersResultMessageDto;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.lotto.numberreceiver.TicketMessageProvider.*;

public class NumberReceiverFacadeTest {

    @Test
    @DisplayName("return success when user gave six numbers")
    public void should_return_success_when_user_gave_six_numbers() {
        // given
        TicketRepository ticketRepository = new InMemoryTicketRepository();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration()
                .createModuleForTests(ticketRepository);
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 6);
        // when
        NumbersResultMessageDto inputNumbers = numberReceiverFacade.inputNumbers(numbers);
        // then
        List<String> messages = List.of(EQUALS_SIX_NUMBERS);
        NumbersResultMessageDto  result = new NumbersResultMessageDto(numbers, messages);
        assertThat(inputNumbers).isEqualTo(result);
    }

    @Test
    @DisplayName("return failed when user gave less than six numbers")
    public void should_return_failed_when_user_gave_less_than_six_numbers() {
        // given
        TicketRepository ticketRepository = new InMemoryTicketRepository();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration()
                .createModuleForTests(ticketRepository);
        Set<Integer> numbers = Set.of(1, 2, 3, 4);
        // when
        NumbersResultMessageDto inputNumbers = numberReceiverFacade.inputNumbers(numbers);
        // then
        List<String> messages = List.of(LESS_THAN_SIX_NUMBERS);
        NumbersResultMessageDto  result = new NumbersResultMessageDto(numbers, messages);
        assertThat(inputNumbers).isEqualTo(result);
    }

    @Test
    @DisplayName("return failed when user gave more than six numbers")
    public void should_return_failed_when_user_gave_more_than_six_numbers() {
        // given
        TicketRepository ticketRepository = new InMemoryTicketRepository();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration()
                .createModuleForTests(ticketRepository);
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
        TicketRepository ticketRepository = new InMemoryTicketRepository();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration()
                .createModuleForTests(ticketRepository);
        Set<Integer> numbers = Set.of(100, 1, 2, 3, 4, -3);
        // when
        NumbersResultMessageDto inputNumbers = numberReceiverFacade.inputNumbers(numbers);
        // then
        List<String> messages = List.of(EQUALS_SIX_NUMBERS);
        NumbersResultMessageDto result = new NumbersResultMessageDto(numbers, messages);
        assertThat(inputNumbers).isEqualTo(result);
    }
}
