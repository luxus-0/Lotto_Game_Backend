package pl.lotto.domain.numberreceiver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.lotto.domain.drawdate.AdjustableClock;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.drawdate.DrawDateFacadeConfiguration;
import pl.lotto.domain.numberreceiver.dto.TicketResultDto;
import pl.lotto.domain.numberreceiver.dto.TicketDto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.lotto.domain.numberreceiver.ValidationResult.*;

class NumberReceiverFacadeTest {
    TicketRepository ticketRepository = new InMemoryTicketRepositoryTestImpl();

    @Test
    public void should_return_six_numbers_message_when_user_gave_6_numbers() {
        // given
        HashGenerable hashGenerator = new HashGeneratorTestImpl();
        AdjustableClock clock = new AdjustableClock(LocalDateTime.of(2023,7,6, 12,0,0).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(clock, hashGenerator, ticketRepository);

        Set<Integer> inputNumbers = Set.of(1,2,3,4,5,6);
        // when
        TicketResultDto actualNumberReceiver = numberReceiverFacade.inputNumbers(inputNumbers);
        // then
        TicketDto actualTicket = actualNumberReceiver.ticketDto();
        TicketResultDto expectedNumberReceiver = new TicketResultDto(actualTicket, EQUALS_SIX_NUMBERS.getInfo());
        assertThat(actualNumberReceiver).isEqualTo(expectedNumberReceiver);
    }

    @Test
    public void should_return_less_than_six_numbers_message_when_user_gave_less_than_6_numbers() {
        // given
        HashGenerable hashGenerator = new HashGeneratorTestImpl();
        AdjustableClock clock = new AdjustableClock(LocalDateTime.now().toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(clock, hashGenerator, ticketRepository);

        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4);
        // when
        TicketResultDto actualNumberReceiver = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        TicketResultDto expectedNumberReceiver = new TicketResultDto(null, LESS_THAN_SIX_NUMBERS.getInfo());

        assertThat(actualNumberReceiver).isEqualTo(expectedNumberReceiver);
    }

    @Test
    public void should_return_more_than_six_number_message_when_user_gave_more_than_6_numbers() {
        // given
        HashGenerable hashGenerator = new HashGeneratorTestImpl();
        AdjustableClock clock = new AdjustableClock(LocalDateTime.now().toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(clock, hashGenerator, ticketRepository);

        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6, 7, 8);
        // when
        TicketResultDto actualNumberReceiver = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        TicketResultDto expectedNumberReceiver = new TicketResultDto(null, MORE_THAN_SIX_NUMBERS.getInfo());

        assertThat(actualNumberReceiver).isEqualTo(expectedNumberReceiver);
    }

    @Test
    public void should_return_out_of_range_message_when_user_gave_at_least_one_number_out_of_range_from_1_to_99() {
        // given
        HashGenerable hashGenerator = new HashGeneratorTestImpl();
        AdjustableClock clock = new AdjustableClock(LocalDateTime.now().toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(clock, hashGenerator, ticketRepository);

        Set<Integer> numbersFromUser = Set.of(1, 2, 100, 4, 5, 12);
        // when
        TicketResultDto actualNumberReceiver = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        TicketResultDto expectedNumberReceiver = new TicketResultDto(null, OUT_OF_RANGE_NUMBERS.getInfo());

        assertThat(actualNumberReceiver).isEqualTo(expectedNumberReceiver);
    }

    @Test
    public void should_return_no_numbers_message_when_user_gave_any_numbers() {
        // given
        HashGenerable hashGenerator = new HashGeneratorTestImpl();
        AdjustableClock clock = new AdjustableClock(LocalDateTime.now().toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(clock, hashGenerator, ticketRepository);

        Set<Integer> numbersFromUser = Set.of();
        // when
        TicketResultDto actualNumberReceiver = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        TicketResultDto expectedNumberReceiver = new TicketResultDto(null, EMPTY_NUMBERS.getInfo());

        assertThat(actualNumberReceiver).isEqualTo(expectedNumberReceiver);
    }

    @Test
    @DisplayName("return out of range message when user gave negative number")
    public void should_return_out_of_range_message_when_user_gave_one_negative_number() {
        // given
        HashGenerable hashGenerator = new HashGeneratorTestImpl();
        AdjustableClock clock = new AdjustableClock(LocalDateTime.now().toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(clock, hashGenerator, ticketRepository);

        Set<Integer> numbersFromUser = Set.of(34, 3, 13, 5, -44, 7);
        // when
        TicketResultDto actualNumberReceiver = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        TicketResultDto expectedNumberReceiver = new TicketResultDto(null, OUT_OF_RANGE_NUMBERS.getInfo());

        assertThat(actualNumberReceiver).isEqualTo(expectedNumberReceiver);
    }

    @Test
    public void should_return_correct_hash() {
        //given
        HashGenerable hashGenerator = new HashGeneratorTestImpl();
        AdjustableClock clock = new AdjustableClock(LocalDateTime.of(2022, 12, 2,12,0,0,0).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(clock, hashGenerator, ticketRepository);

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
        AdjustableClock clock = new AdjustableClock(LocalDateTime.of(2023, 2, 18, 12, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(clock, hashGenerator, ticketRepository);

        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        LocalDateTime expectedDrawDate = LocalDateTime.of(2023, 2, 25, 12, 0, 0);

        // when
        TicketResultDto actualNumberReceiver = numberReceiverFacade.inputNumbers(numbersFromUser);
        LocalDateTime actualDrawDate = actualNumberReceiver.ticketDto().drawDate();
        // then
        assertThat(actualDrawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void should_return_next_Saturday_draw_date_when_date_is_Saturday_afternoon() {
        //given
        AdjustableClock clock = new AdjustableClock(LocalDateTime.of(2023, 2, 25, 12, 0, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        DrawDateFacade drawDateFacade = new DrawDateFacadeConfiguration()
                .createModuleForTests(clock);

        LocalDateTime expectedDrawDate = LocalDateTime.of(2023, 3, 4, 12, 0, 0,0);
        //when
        LocalDateTime actualDrawDate = drawDateFacade.retrieveNextDrawDate();
        //then
        assertThat(actualDrawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void should_return_next_Saturday_draw_date_when_date_is_Saturday_noon() {
        //given
        HashGenerable hashGenerator = new HashGeneratorTestImpl();
        AdjustableClock clock = new AdjustableClock(LocalDateTime.of(2023, 2, 20, 12, 0, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(clock, hashGenerator, ticketRepository);

        Set<Integer> inputNumbers = Set.of(1, 2, 3, 4, 5, 6);
        //when
        TicketResultDto actualNumberReceiver = numberReceiverFacade.inputNumbers(inputNumbers);
        LocalDateTime actualDrawDate = actualNumberReceiver.ticketDto().drawDate();
        //then
        LocalDateTime expectedDrawDate = LocalDateTime.of(2023, 2, 25, 12, 0, 0, 0);

        assertThat(actualDrawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void should_return_tickets_with_correct_draw_date() {
        //given
        HashGenerator hashGenerator = new HashGenerator();
        AdjustableClock clock = new AdjustableClock(LocalDateTime.of(2023, 2, 11, 12,0,0,0).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        DrawDateFacade drawDateFacade = new DrawDateFacadeConfiguration()
                .createModuleForTests(clock);

        TicketDto expectedTicket = TicketDto.builder()
                .hash(hashGenerator.getHash())
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(LocalDateTime.of(2023,2,18,12,0,0,0))
                .build();
        // when
        LocalDateTime actualDrawDate = drawDateFacade.retrieveNextDrawDate();
        // then
        assertThat(expectedTicket.drawDate()).isEqualTo(actualDrawDate);
    }

    @Test
    public void should_return_empty_collection_if_there_are_no_ticket() {
        //given
        HashGenerable hashGenerator = new HashGeneratorTestImpl();
        AdjustableClock clock = new AdjustableClock(LocalDateTime.of(2022,10,8,12,0,0).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(clock, hashGenerator, ticketRepository);
        DrawDateFacade drawDateFacade = new DrawDateFacadeConfiguration()
                .createModuleForTests(clock);
        //when
        List<TicketDto> actualTickets = numberReceiverFacade.retrieveAllTicketByDrawDate(drawDateFacade.retrieveNextDrawDate());
        //then
        assertThat(actualTickets).isEqualTo(Collections.EMPTY_LIST);
    }

    @Test
    public void it_should_return_empty_collections_if_given_date_is_after_next_drawDate() {
        // given
        HashGenerable hashGenerator = new HashGeneratorTestImpl();
        LocalDateTime actualDrawDate = LocalDateTime.of(2022,10,8,12,0,0);
        AdjustableClock clock = new AdjustableClock(actualDrawDate.toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(clock, hashGenerator, ticketRepository);

        LocalDateTime expectedDrawDate = LocalDateTime.of(2022, 10, 15, 12, 0, 0);
        // when
        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketByDrawDate(expectedDrawDate);
        // then
        assertThat(allTicketsByDate).isEmpty();
        assertThat(expectedDrawDate).isAfter(actualDrawDate);
    }

    @Test
    public void should_return_next_draw_date() {
        // given
        AdjustableClock clock = new AdjustableClock(LocalDateTime.of(2023,4,5,12,0,0,0).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        DrawDateFacade drawDateFacade = new DrawDateFacadeConfiguration()
                .createModuleForTests(clock);

        LocalDateTime expectedDrawDate = LocalDateTime.of(2023, 4, 8, 12, 0, 0, 0);
        // when
        LocalDateTime actualDrawDate = drawDateFacade.retrieveNextDrawDate();
        // then
        assertThat(actualDrawDate).isEqualTo(expectedDrawDate);
    }
}
