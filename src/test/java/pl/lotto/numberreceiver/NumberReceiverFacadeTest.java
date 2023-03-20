package pl.lotto.numberreceiver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.lotto.numberreceiver.dto.NumberResultDto;
import pl.lotto.numberreceiver.dto.TicketDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.lotto.numberreceiver.NumbersReceiverMessageProvider.*;

class NumberReceiverFacadeTest {

    private final Clock clock = Clock.systemUTC();
    NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration().createModuleForTests(clock);

    @Test
    @DisplayName("return 6 numbers message when user gave correct numbers")
    public void should_return_six_numbers_message_when_user_gave_6_numbers() {
        // given
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        // when
        NumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        assertThat(result.message()).isEqualTo(EQUALS_SIX_NUMBERS);
    }

    @Test
    @DisplayName("return less than 6 numbers message when user gave less than 6 numbers")
    public void should_return_less_than_six_numbers_message_when_user_gave_less_than_6_numbers() {
        // given
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4);
        // when
        NumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        assertThat(result.message()).isEqualTo(LESS_THAN_SIX_NUMBERS);
    }

    @Test
    @DisplayName("return more than 6 number message when user gave more than 6 numbers")
    public void should_return_more_than_six_number_message_when_user_gave_more_than_6_numbers() {
        // given
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6, 7, 8);
        // when
        NumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        assertThat(result.message()).isEqualTo(MORE_THAN_SIX_NUMBERS);
    }

    @Test
    @DisplayName("return out of range message when user gave one number out of range")
    public void should_return_out_of_range_message_when_user_gave_at_least_one_number_out_of_range_from_1_to_99() {
        // given
        Set<Integer> numbersFromUser = Set.of(1, 2, 100, 4, 5, 12);
        // when
        NumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        assertThat(result.message()).isEqualTo(OUT_OF_RANGE_NUMBERS);
    }

    @Test
    @DisplayName("return no numbers message when user gave any numbers")
    public void should_return_no_numbers_message_when_user_gave_any_numbers() {
        // given
        Set<Integer> numbersFromUser = Set.of();
        // when
        NumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        assertThat(result.message()).isEqualTo(NO_NUMBERS);
    }

    @Test
    @DisplayName("return out of range message when user gave negative number")
    public void should_return_out_of_range_message_when_user_gave_one_negative_number() {
        // given
        Set<Integer> numbersFromUser = Set.of(34, 3, 13, 5, -44, 7);
        // when
        NumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        assertThat(result.message()).isEqualTo(OUT_OF_RANGE_NUMBERS);
    }

    @Test
    @DisplayName("return save to database when user gave 6 numbers")
    public void should_save_to_database_when_user_gave_6_numbers(){
        //given
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        NumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);
        LocalDateTime drawDate = LocalDateTime.of(2023,3,25,12,0,0,0);
        // when
        List<TicketDto> ticketDtos = numberReceiverFacade.retrieveAllTicketByDrawDate(drawDate);
        //then
        assertThat(ticketDtos).contains(
                TicketDto.builder()
                        .hash(result.ticketId())
                        .drawDate(drawDate)
                        .numbersFromUser(result.numbersFromUser())
                        .build()
        );
    }
}
