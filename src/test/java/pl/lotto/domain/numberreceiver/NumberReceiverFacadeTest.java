package pl.lotto.domain.numberreceiver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.lotto.numberreceiver.*;
import pl.lotto.numberreceiver.dto.NumberReceiverResultDto;
import pl.lotto.numberreceiver.dto.TicketDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.lotto.numberreceiver.ValidationResult.EQUALS_SIX_NUMBERS;

class NumberReceiverFacadeTest {
    private final TicketRepository ticketRepository = new TicketRepositoryTestImpl();
    Clock clock = Clock.fixed(LocalDateTime.of(2023,2,18,12,0,0).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());



    @Test
    @DisplayName("return 6 numbers message when user gave correct numbers")
    public void should_return_six_numbers_message_when_user_gave_6_numbers() {
        // given
        HashGenerable hashGenerator = new HashGeneratorTestImpl();

        DateTimeDrawGenerator dateTimeDrawGenerator = new DateTimeDrawGenerator(clock);
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration()
                .createModuleForTests(clock, hashGenerator, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        LocalDateTime nextDrawDate = dateTimeDrawGenerator.readNextDrawDate();

        TicketDto createdTicket = TicketDto.builder()
                .hash(hashGenerator.getHash())
                .numbersFromUser(numbersFromUser)
                .drawDate(nextDrawDate)
                .build();

        // when
        NumberReceiverResultDto response = numberReceiverFacade.inputNumbers(numbersFromUser);

        // then
        NumberReceiverResultDto expectedResponse = new NumberReceiverResultDto(createdTicket, EQUALS_SIX_NUMBERS.getInfo());
        assertThat(response.message()).isEqualTo(expectedResponse.message());
    }

    /*@Test
    @DisplayName("return less than 6 numbers message when user gave less than 6 numbers")
    public void should_return_less_than_six_numbers_message_when_user_gave_less_than_6_numbers() {
        // given
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4);
        // when
        NumberReceiverResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        assertThat(result.message()).isEqualTo(LESS_THAN_SIX_NUMBERS.info);
    }

    @Test
    @DisplayName("return more than 6 number message when user gave more than 6 numbers")
    public void should_return_more_than_six_number_message_when_user_gave_more_than_6_numbers() {
        // given
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6, 7, 8);
        // when
        NumberReceiverResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        assertThat(result.message()).isEqualTo(MORE_THAN_SIX_NUMBERS.info);
    }

    @Test
    @DisplayName("return out of range message when user gave one number out of range")
    public void should_return_out_of_range_message_when_user_gave_at_least_one_number_out_of_range_from_1_to_99() {
        // given
        Set<Integer> numbersFromUser = Set.of(1, 2, 100, 4, 5, 12);
        // when
        NumberReceiverResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        assertThat(result.message()).isEqualTo(OUT_OF_RANGE_NUMBERS.info);
    }

    @Test
    @DisplayName("return no numbers message when user gave any numbers")
    public void should_return_no_numbers_message_when_user_gave_any_numbers() {
        // given
        Set<Integer> numbersFromUser = Set.of();
        // when
        NumberReceiverResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        assertThat(result.message()).isEqualTo(EMPTY_NUMBERS.info);
    }

    @Test
    @DisplayName("return out of range message when user gave negative number")
    public void should_return_out_of_range_message_when_user_gave_one_negative_number() {
        // given
        Set<Integer> numbersFromUser = Set.of(34, 3, 13, 5, -44, 7);
        // when
        NumberReceiverResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        assertThat(result.message()).isEqualTo(OUT_OF_RANGE_NUMBERS.info);
    }

    @Test
    @DisplayName("return save to database when user gave 6 numbers")
    public void should_save_to_database_when_user_gave_6_numbers(){
        //given
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        LocalDateTime drawDate = numberReceiverFacade.retrieveNextDrawDate();
        List<TicketDto> ticketDtos = numberReceiverFacade.retrieveAllTicketByDrawDate(drawDate);
        //when
        NumberReceiverResultDto resultDto = numberReceiverFacade.inputNumbers(numbersFromUser);

        assertThat(ticketDtos).contains(
                TicketDto.builder()
                        .hash(hashGenerator.getHash())
                        .drawDate(drawDate)
                        .numbersFromUser(numbersFromUser)
                        .build()
        );
    }*/
}
