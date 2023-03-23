package pl.lotto.domain.numberreceiver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.lotto.domain.AdjustableClock;
import pl.lotto.domain.numberreceiver.dto.NumberReceiverResultDto;
import pl.lotto.domain.numberreceiver.dto.TicketDto;

import java.time.*;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.lotto.domain.numberreceiver.ValidationResult.*;

class NumberReceiverFacadeTest {

    private final HashGenerable hashGenerator;
    private final NumberReceiverFacade numberReceiverFacade;
    private final TicketRepository ticketRepository;

    NumberReceiverFacadeTest() {
        this.ticketRepository = new InMemoryTicketRepositoryTestImpl();
        this.hashGenerator = new HashGeneratorTestImpl();
        AdjustableClock clock = new AdjustableClock(LocalDateTime.of(2023, 2, 15, 14, 0, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        this.numberReceiverFacade = new NumberReceiverFacadeConfiguration()
                .createModuleForTests(clock, hashGenerator, ticketRepository);
    }

    @Test
    public void should_return_six_numbers_message_when_user_gave_6_numbers() {
        // given
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        AdjustableClock clock = new AdjustableClock(LocalDateTime.of(2023, 2, 15, 11, 0, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        DateTimeDrawGenerator date = new DateTimeDrawGenerator(clock);

        TicketDto createdTicket = TicketDto.builder()
                .hash(hashGenerator.getHash())
                .numbersFromUser(numbersFromUser)
                .drawDate(date.generateNextDrawDate())
                .build();
        // when
        NumberReceiverResultDto response = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        NumberReceiverResultDto expectedResponse = new NumberReceiverResultDto(createdTicket, EQUALS_SIX_NUMBERS.getInfo());
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void should_return_less_than_six_numbers_message_when_user_gave_less_than_6_numbers() {
        // given
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4);
        // when
        NumberReceiverResultDto actualResult = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        NumberReceiverResultDto expectedResult = new NumberReceiverResultDto(null, LESS_THAN_SIX_NUMBERS.getInfo());

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void should_return_more_than_six_number_message_when_user_gave_more_than_6_numbers() {
        // given
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6, 7, 8);
        // when
        NumberReceiverResultDto actualResult = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        NumberReceiverResultDto expectedResult = new NumberReceiverResultDto(null, MORE_THAN_SIX_NUMBERS.getInfo());

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void should_return_out_of_range_message_when_user_gave_at_least_one_number_out_of_range_from_1_to_99() {
        // given
        Set<Integer> numbersFromUser = Set.of(1, 2, 100, 4, 5, 12);
        // when
        NumberReceiverResultDto actualResult = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        NumberReceiverResultDto expectedResult = new NumberReceiverResultDto(null, OUT_OF_RANGE_NUMBERS.getInfo());

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void should_return_no_numbers_message_when_user_gave_any_numbers() {
        // given
        Set<Integer> numbersFromUser = Set.of();
        // when
        NumberReceiverResultDto actualResult = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        NumberReceiverResultDto expectedResult = new NumberReceiverResultDto(null, EMPTY_NUMBERS.getInfo());

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("return out of range message when user gave negative number")
    public void should_return_out_of_range_message_when_user_gave_one_negative_number() {
        // given
        Set<Integer> numbersFromUser = Set.of(34, 3, 13, 5, -44, 7);
        // when
        NumberReceiverResultDto actualResult = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        NumberReceiverResultDto expectedResult = new NumberReceiverResultDto(null, OUT_OF_RANGE_NUMBERS.getInfo());

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void should_return_correct_hash() {
        //given
        Set<Integer> inputNumber = Set.of(1, 2, 3, 4, 5, 6);
        String expectedHash = "1234567";
        //when
        String actualHash = numberReceiverFacade.inputNumbers(inputNumber).ticketDto().hash();
        //then
        assertThat(actualHash).isEqualTo(expectedHash);
        assertThat(actualHash).isNotNull();
        assertThat(actualHash).hasSize(7);
    }

    @Test
    public void should_return_correct_draw_date() {
        // given
        HashGenerable hashGenerator = new HashGeneratorTestImpl();
        AdjustableClock clock = new AdjustableClock(LocalDateTime.of(2022, 2, 19, 14, 0, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration()
                .createModuleForTests(clock, hashGenerator, ticketRepository);

        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        // when
        LocalDateTime testedDrawDate = numberReceiverFacade.inputNumbers(numbersFromUser).ticketDto().drawDate();

        // then
        LocalDateTime expectedDrawDate = LocalDateTime.of(2022, 2, 26, 12, 0, 0);
        assertThat(testedDrawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void should_return_next_Saturday_draw_date_when_date_is_Saturday_afternoon() {
        //given
        HashGenerable hashGenerator = new HashGeneratorTestImpl();
        AdjustableClock clock = new AdjustableClock(LocalDateTime.of(2023, 2, 15, 14, 0, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration()
                .createModuleForTests(clock, hashGenerator, ticketRepository);

        Set<Integer> inputNumbers = Set.of(1, 2, 3, 4, 5, 6);

        //when
        LocalDateTime actualDrawDate = numberReceiverFacade.inputNumbers(inputNumbers).ticketDto().drawDate();
        //then
        LocalDateTime expectedDrawDate = LocalDateTime.of(2023, 2, 18, 12, 0, 0, 0);

        assertThat(actualDrawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void should_return_next_Saturday_draw_date_when_date_is_Saturday_noon() {
        //given
        HashGenerable hashGenerator = new HashGeneratorTestImpl();
        AdjustableClock clock = new AdjustableClock(LocalDateTime.of(2023, 2, 20, 12, 0, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration()
                .createModuleForTests(clock, hashGenerator, ticketRepository);

        Set<Integer> inputNumbers = Set.of(1, 2, 3, 4, 5, 6);

        //when
        LocalDateTime actualDrawDate = numberReceiverFacade.inputNumbers(inputNumbers).ticketDto().drawDate();
        //then
        LocalDateTime expectedDrawDate = LocalDateTime.of(2023, 2, 25, 12, 0, 0, 0);

        assertThat(actualDrawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void should_return_tickets_with_correct_draw_date() {
        HashGenerable hashGenerator = new HashGenerator();

        Instant fixedInstant = LocalDateTime.of(2022, 12, 15, 12, 0, 0).toInstant(ZoneOffset.UTC);
        ZoneId of = ZoneId.of("Europe/London");
        AdjustableClock clock = new AdjustableClock(fixedInstant, of);
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration().createModuleForTests(clock, hashGenerator, ticketRepository);
        NumberReceiverResultDto numberReceiverResponseDto = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        clock.plusDays(1);
        NumberReceiverResultDto numberReceiverResponseDto1 = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        TicketDto ticketDto = numberReceiverResponseDto.ticketDto();
        TicketDto ticketDto1 = numberReceiverResponseDto1.ticketDto();
        LocalDateTime drawDate = numberReceiverResponseDto.ticketDto().drawDate();
        // when
        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketByDrawDate(drawDate);
        // then
        assertThat(allTicketsByDate).containsOnly(ticketDto, ticketDto1);
    }

    @Test
    public void should_return_empty_collection_if_there_are_no_ticket() {
        //given
        HashGenerable hashGenerator = new HashGenerator();
        Instant fixedInstant = LocalDateTime.of(2022, 12, 19, 12, 0, 0).toInstant(ZoneOffset.UTC);
        ZoneId of = ZoneId.of("Europe/London");
        AdjustableClock clock = new AdjustableClock(fixedInstant, of);
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration().createModuleForTests(clock, hashGenerator, ticketRepository);

        LocalDateTime dateTimeDraw = LocalDateTime.now(clock);

        //when
        List<TicketDto> ticketsByDrawDate = numberReceiverFacade.retrieveAllTicketByDrawDate(dateTimeDraw);
        //then
        assertThat(ticketsByDrawDate).isEqualTo(Collections.EMPTY_LIST);
    }

    @Test
    public void it_should_return_empty_collections_if_given_date_is_after_next_drawDate() {
        // given
        HashGenerable hashGenerator = new HashGenerator();
        Instant fixedInstant = LocalDateTime.of(2022, 12, 19, 12, 0, 0).toInstant(ZoneOffset.UTC);
        ZoneId of = ZoneId.of("Europe/London");
        AdjustableClock clock = new AdjustableClock(fixedInstant, of);
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration().createModuleForTests(clock, hashGenerator, ticketRepository);
        NumberReceiverResultDto numberReceiverResponseDto = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));

        LocalDateTime drawDate = numberReceiverResponseDto.ticketDto().drawDate();

        // when
        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketByDrawDate(drawDate.plusWeeks(1L));
        // then
        assertThat(allTicketsByDate).isEmpty();
    }

    @Test
    public void should_return_next_draw_date() {
        // given
        HashGenerable hashGenerator = new HashGenerator();
        Instant fixedInstant = LocalDateTime.of(2022, 12, 19, 12, 0, 0, 0).toInstant(ZoneOffset.UTC);
        ZoneId of = ZoneId.systemDefault();
        AdjustableClock clock = new AdjustableClock(fixedInstant, of);
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration().createModuleForTests(clock, hashGenerator, ticketRepository);

        // when
        LocalDateTime testedDrawDate = numberReceiverFacade.createDrawDateForTicket();

        // then
        LocalDateTime expectedDrawDate = LocalDateTime.of(2022, 12, 24, 12, 0, 0, 0);
        assertThat(testedDrawDate).isEqualTo(expectedDrawDate);
    }

}
