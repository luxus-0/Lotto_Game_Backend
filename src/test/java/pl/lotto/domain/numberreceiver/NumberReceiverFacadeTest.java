package pl.lotto.domain.numberreceiver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.lotto.domain.drawdate.AdjustableClock;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.drawdate.DrawDateFacadeConfiguration;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.numberreceiver.dto.TicketResponseDto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.lotto.domain.numberreceiver.TicketValidationResult.*;
class NumberReceiverFacadeTest {

    TicketRepository ticketRepository = mock(TicketRepository.class);
    TicketIdGenerator hashGenerator = new HashGeneratorTestImpl();
    AdjustableClock clock = new AdjustableClock(LocalDateTime.of(2023,7,6, 12,0,0).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());

    @Test
    public void should_return_six_numbers_message_when_user_gave_6_numbers() {
        // given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(clock, hashGenerator, ticketRepository);

        Set<Integer> inputNumbers = Set.of(1,2,3,4,5,6);
        // when
        when(ticketRepository.save(any(Ticket.class)))
                .thenReturn(new Ticket(UUID.randomUUID().toString(), Set.of(1,2,3,4,5,6), LocalDateTime.now(), ""));

        TicketResponseDto actualNumberReceiver = numberReceiverFacade.inputNumbers(inputNumbers);
        // then
        assertThat(actualNumberReceiver.message()).isEqualTo(EQUALS_SIX_NUMBERS.getInfo());
    }

    @Test
    public void should_return_less_than_six_numbers_message_when_user_gave_less_than_6_numbers() {
        // given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(clock, hashGenerator, ticketRepository);

        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4);
        // when
        TicketResponseDto actualNumberReceiver = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        assertThat(actualNumberReceiver.message()).isEqualTo(LESS_THAN_SIX_NUMBERS.getInfo());
    }

    @Test
    public void should_return_more_than_six_number_message_when_user_gave_more_than_6_numbers() {
        // given
        TicketIdGenerator hashGenerator = new HashGeneratorTestImpl();
        AdjustableClock clock = new AdjustableClock(LocalDateTime.now().toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(clock, hashGenerator, ticketRepository);

        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6, 7, 8);
        // when
        TicketResponseDto actualNumberReceiver = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        TicketResponseDto expectedNumberReceiver = new TicketResponseDto(null, MORE_THAN_SIX_NUMBERS.getInfo());

        assertThat(actualNumberReceiver).isEqualTo(expectedNumberReceiver);
    }

    @Test
    public void should_return_out_of_range_message_when_user_gave_at_least_one_number_out_of_range_from_1_to_99() {
        // given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(clock, hashGenerator, ticketRepository);

        Set<Integer> numbersFromUser = Set.of(1, 2, 100, 4, 5, 12);
        // when
        TicketResponseDto actualNumberReceiver = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        TicketResponseDto expectedNumberReceiver = new TicketResponseDto(null, OUT_OF_RANGE_NUMBERS.getInfo());

        assertThat(actualNumberReceiver).isEqualTo(expectedNumberReceiver);
    }

    @Test
    public void should_return_no_numbers_message_when_user_gave_any_numbers() {
        // given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(clock, hashGenerator, ticketRepository);

        Set<Integer> numbersFromUser = Set.of();
        // when
        TicketResponseDto actualNumberReceiver = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        TicketResponseDto expectedNumberReceiver = new TicketResponseDto(null, EMPTY_NUMBERS.getInfo());

        assertThat(actualNumberReceiver).isEqualTo(expectedNumberReceiver);
    }

    @Test
    @DisplayName("return out of range message when user gave negative number")
    public void should_return_out_of_range_message_when_user_gave_one_negative_number() {
        // given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(clock, hashGenerator, ticketRepository);

        Set<Integer> numbersFromUser = Set.of(34, 3, 13, 5, -44, 7);
        // when
        TicketResponseDto actualNumberReceiver = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        TicketResponseDto expectedNumberReceiver = new TicketResponseDto(null, OUT_OF_RANGE_NUMBERS.getInfo());

        assertThat(actualNumberReceiver).isEqualTo(expectedNumberReceiver);
    }

    @Test
    public void should_return_correct_hash() {
        //given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(clock, hashGenerator, ticketRepository);

        Set<Integer> inputNumbers = Set.of(1,2,3,4,5,6);
        //when

        when(ticketRepository.save(any(Ticket.class)))
                .thenReturn(new Ticket("123456", Set.of(1,2,3,4,5,6), LocalDateTime.now(), ""));

        TicketResponseDto actualTicket = numberReceiverFacade.inputNumbers(inputNumbers);
        String actualTicketId = actualTicket.ticket().ticketId();
        //then
        assertThat(actualTicket).isNotNull();
        assertThat(actualTicketId).isEqualTo("123456");
        assertThat(actualTicket.ticket().numbers()).hasSize(6);
    }

    @Test
    public void should_return_correct_draw_date() {
        // given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(clock, hashGenerator, ticketRepository);

        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        LocalDateTime expectedDrawDate = LocalDateTime.of(2022, 11, 17, 12, 0, 0);

        // when
        when(ticketRepository.save(any(Ticket.class)))
                .thenReturn(new Ticket(UUID.randomUUID().toString(), Set.of(1,2,3,4,5,6), LocalDateTime.of(2022, 11, 17, 12, 0, 0), ""));

        TicketResponseDto actualTicket = numberReceiverFacade.inputNumbers(numbersFromUser);
        LocalDateTime actualDrawDate = actualTicket.ticket().drawDate();
        // then
        assertThat(actualDrawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void should_return_next_Saturday_draw_date_when_date_is_Saturday_afternoon() {
        //given
        DrawDateFacade drawDateFacade = new DrawDateFacadeConfiguration()
                .drawDateFacade(clock);

        LocalDateTime expectedDrawDate = LocalDateTime.of(2023, 7, 8, 12, 0, 0,0);
        //when
        LocalDateTime actualDrawDate = drawDateFacade.retrieveNextDrawDate();
        //then
        assertThat(actualDrawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void should_return_next_Saturday_draw_date_when_date_is_Saturday_noon() {
        //given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(clock, hashGenerator, ticketRepository);

        Set<Integer> inputNumbers = Set.of(1, 2, 3, 4, 5, 6);
        //when
        when(ticketRepository.save(any(Ticket.class)))
                .thenReturn(new Ticket(UUID.randomUUID().toString(), Set.of(1,2,3,4,5,6), LocalDateTime.of(2023, 11, 17, 12, 0, 0, 0), ""));

        TicketResponseDto actualTicket = numberReceiverFacade.inputNumbers(inputNumbers);
        LocalDateTime actualDrawDate = actualTicket.ticket().drawDate();
        //then
        LocalDateTime expectedDrawDate = LocalDateTime.of(2023, 11, 17, 12, 0, 0, 0);

        assertThat(actualDrawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void should_return_tickets_with_correct_draw_date() {
        //given
        DrawDateFacade drawDateFacade = new DrawDateFacadeConfiguration()
                .drawDateFacade(clock);

        TicketDto expectedTicket = TicketDto.builder()
                .ticketId(hashGenerator.generateTicketId())
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(LocalDateTime.of(2023,7,8,12,0,0,0))
                .build();
        // when
        LocalDateTime actualDrawDate = drawDateFacade.retrieveNextDrawDate();
        // then
        assertThat(expectedTicket.drawDate()).isEqualTo(actualDrawDate);
    }

    @Test
    public void should_return_empty_collection_if_there_are_no_ticket() {
        //given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(clock, hashGenerator, ticketRepository);
        DrawDateFacade drawDateFacade = new DrawDateFacadeConfiguration()
                .drawDateFacade(clock);
        //when
        List<TicketDto> actualTickets = numberReceiverFacade.retrieveAllTicketByDrawDate(drawDateFacade.retrieveNextDrawDate());
        //then
        assertThat(actualTickets).isEqualTo(Collections.EMPTY_LIST);
    }

    @Test
    public void it_should_return_empty_collections_if_given_date_is_after_next_drawDate() {
        // given
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
        DrawDateFacade drawDateFacade = new DrawDateFacadeConfiguration()
                .drawDateFacade(clock);

        LocalDateTime expectedDrawDate = LocalDateTime.of(2023, 7, 8, 12, 0, 0, 0);
        // when
        LocalDateTime actualDrawDate = drawDateFacade.retrieveNextDrawDate();
        // then
        assertThat(actualDrawDate).isEqualTo(expectedDrawDate);
    }
}
