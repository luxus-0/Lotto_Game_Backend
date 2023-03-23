package pl.lotto.domain.numberreceiver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.lotto.domain.AdjustableClock;
import pl.lotto.domain.numberreceiver.dto.NumberReceiverResultDto;
import pl.lotto.domain.numberreceiver.dto.TicketDto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.lotto.domain.numberreceiver.ValidationResult.*;

class NumberReceiverFacadeTest {

    private final HashGenerable hashGenerator;
    private final DateTimeDrawGenerator dateTimeDrawGenerator;

    private final NumberReceiverFacade numberReceiverFacade;

    NumberReceiverFacadeTest() {
        TicketRepository ticketRepository = new InMemoryTicketRepositoryTestImpl();
        this.hashGenerator = new HashGeneratorTestImpl();
        AdjustableClock clock = new AdjustableClock(LocalDateTime.of(2023, 2, 15, 11, 0, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        this.dateTimeDrawGenerator = new DateTimeDrawGenerator(clock);
        this.numberReceiverFacade = new NumberReceiverFacadeConfiguration()
                .createModuleForTests(hashGenerator, ticketRepository);
    }

    @Test
    @DisplayName("return 6 numbers message when user gave correct numbers")
    public void should_return_six_numbers_message_when_user_gave_6_numbers() {
        // given
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        LocalDateTime nextDrawDate = dateTimeDrawGenerator.generateNextDrawDate();

        TicketDto createdTicket = TicketDto.builder()
                .hash(hashGenerator.getHash())
                .numbersFromUser(numbersFromUser)
                .drawDate(nextDrawDate)
                .build();
        // when
        NumberReceiverResultDto response = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        NumberReceiverResultDto expectedResponse = new NumberReceiverResultDto(createdTicket, EQUALS_SIX_NUMBERS.getInfo());
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("return less than 6 numbers message when user gave less than 6 numbers")
    public void should_return_less_than_six_numbers_message_when_user_gave_less_than_6_numbers() {
        // given
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4);
        // when
        NumberReceiverResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        assertThat(result.message()).isEqualTo(LESS_THAN_SIX_NUMBERS.getInfo());
    }

    @Test
    @DisplayName("return more than 6 number message when user gave more than 6 numbers")
    public void should_return_more_than_six_number_message_when_user_gave_more_than_6_numbers() {
        // given
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6, 7, 8);
        // when
        NumberReceiverResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        assertThat(result.message()).isEqualTo(MORE_THAN_SIX_NUMBERS.getInfo());
    }

    @Test
    @DisplayName("return out of range message when user gave one number out of range")
    public void should_return_out_of_range_message_when_user_gave_at_least_one_number_out_of_range_from_1_to_99() {
        // given
        Set<Integer> numbersFromUser = Set.of(1, 2, 100, 4, 5, 12);
        // when
        NumberReceiverResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        assertThat(result.message()).isEqualTo(OUT_OF_RANGE_NUMBERS.getInfo());
    }

    @Test
    @DisplayName("return no numbers message when user gave any numbers")
    public void should_return_no_numbers_message_when_user_gave_any_numbers() {
        // given
        Set<Integer> numbersFromUser = Set.of();
        // when
        NumberReceiverResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        assertThat(result.message()).isEqualTo(EMPTY_NUMBERS.getInfo());
    }

    @Test
    @DisplayName("return out of range message when user gave negative number")
    public void should_return_out_of_range_message_when_user_gave_one_negative_number() {
        // given
        Set<Integer> numbersFromUser = Set.of(34, 3, 13, 5, -44, 7);
        // when
        NumberReceiverResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        assertThat(result.message()).isEqualTo(OUT_OF_RANGE_NUMBERS.getInfo());
    }
}
