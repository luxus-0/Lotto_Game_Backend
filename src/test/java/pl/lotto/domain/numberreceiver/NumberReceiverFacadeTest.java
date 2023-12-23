package pl.lotto.domain.numberreceiver;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.lotto.domain.drawdate.AdjustableClock;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.drawdate.DrawDateFacadeConfiguration;
import pl.lotto.domain.numberreceiver.dto.InputNumbersRequestDto;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.numberreceiver.dto.TicketResponseDto;
import pl.lotto.domain.numberreceiver.exceptions.InputNumbersNotFoundException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.lotto.domain.numberreceiver.NumberReceiverValidationResult.*;

@Log4j2
class NumberReceiverFacadeTest {

    TicketRepository ticketRepository = mock(TicketRepository.class);
    TicketUUIDGenerator ticketUUIDGenerator = new TicketUUIDGeneratorTestImpl();
    AdjustableClock clock = new AdjustableClock(LocalDateTime.of(2023, 12, 13, 12, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
    DrawDateFacade drawDateFacade = mock(DrawDateFacade.class);
    @Test
    public void should_return_six_numbers_message_when_user_gave_6_numbers() {
        // given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(clock, ticketUUIDGenerator, ticketRepository);

        InputNumbersRequestDto inputNumbersRequest = new InputNumbersRequestDto(Set.of(1, 2, 3, 4, 5, 6));

        // when
        when(ticketRepository.save(any(Ticket.class)))
                .thenReturn(Ticket.builder()
                        .ticketUUID("12345")
                        .inputNumbers(inputNumbersRequest.inputNumbers())
                        .drawDate(LocalDateTime.of(2023, 12, 13, 12, 0, 0))
                        .message(EQUALS_SIX_NUMBERS.getInfo())
                        .build());

        TicketResponseDto actualNumberReceiver = numberReceiverFacade.inputNumbers(inputNumbersRequest);
        // then
        assertThat(actualNumberReceiver.message()).isEqualTo(EQUALS_SIX_NUMBERS.getInfo());
    }

    @Test
    public void should_return_less_than_six_numbers_message_when_user_gave_less_than_6_numbers() {
        // given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(clock, ticketUUIDGenerator, ticketRepository);

        InputNumbersRequestDto inputNumbersRequest = new InputNumbersRequestDto(Set.of(1, 2, 3, 4));
        // when
        when(ticketRepository.save(any(Ticket.class)))
                .thenReturn(Ticket.builder()
                        .ticketUUID("12345")
                        .inputNumbers(inputNumbersRequest.inputNumbers())
                        .drawDate(LocalDateTime.of(2023, 12, 13, 12, 0, 0))
                        .message(LESS_THAN_SIX_NUMBERS.getInfo())
                        .build());
        // when
        TicketResponseDto actualNumberReceiver = numberReceiverFacade.inputNumbers(inputNumbersRequest);
        // then
        assertThat(actualNumberReceiver.message()).isEqualTo(LESS_THAN_SIX_NUMBERS.getInfo());
    }

    @Test
    public void should_return_more_than_six_number_message_when_user_gave_more_than_6_numbers() {
        // given
        AdjustableClock clock = new AdjustableClock(LocalDateTime.now().toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(clock, ticketUUIDGenerator, ticketRepository);

        InputNumbersRequestDto inputNumbersRequest = new InputNumbersRequestDto(Set.of(1, 2, 3, 4, 5, 6, 7, 8));
        // when
        when(ticketRepository.save(any(Ticket.class)))
                .thenReturn(Ticket.builder()
                        .ticketUUID("12345")
                        .inputNumbers(inputNumbersRequest.inputNumbers())
                        .drawDate(LocalDateTime.of(2023, 12, 13, 12, 0, 0))
                        .message(MORE_THAN_SIX_NUMBERS.getInfo())
                        .build());
        // when
        TicketResponseDto actualNumberReceiver = numberReceiverFacade.inputNumbers(inputNumbersRequest);
        // then
        assertThat(actualNumberReceiver.message()).isEqualTo(MORE_THAN_SIX_NUMBERS.getInfo());
    }

    @Test
    public void should_return_out_of_range_message_when_user_gave_at_least_one_number_out_of_range_from_1_to_99() {
        // given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(clock, ticketUUIDGenerator, ticketRepository);

        InputNumbersRequestDto inputNumbersRequest = new InputNumbersRequestDto(Set.of(100, 200, 300, 400, 500, 600));

        when(ticketRepository.save(any(Ticket.class)))
                .thenReturn(Ticket.builder()
                        .ticketUUID("12345")
                        .inputNumbers(inputNumbersRequest.inputNumbers())
                        .drawDate(LocalDateTime.of(2023, 12, 13, 12, 0, 0))
                        .message(OUT_OF_RANGE_NUMBERS.getInfo())
                        .build());
        // when
        TicketResponseDto actualNumberReceiver = numberReceiverFacade.inputNumbers(inputNumbersRequest);
        // then
        assertThat(actualNumberReceiver.message()).isEqualTo(OUT_OF_RANGE_NUMBERS.getInfo());
    }

    @Test
    public void should_throws_input_numbers_not_found_exception_when_input_numbers_are_empty() {
        // given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(clock, ticketUUIDGenerator, ticketRepository);

        InputNumbersRequestDto inputNumbersRequest = new InputNumbersRequestDto(Set.of());

        // when && then
        RuntimeException exception = assertThrows(InputNumbersNotFoundException.class,
                () -> numberReceiverFacade.inputNumbers(inputNumbersRequest));

        assertThat(exception.getMessage()).isEqualTo("InputNumbers not found");

    }

    @Test
    @DisplayName("return out of range message when user gave negative number")
    public void should_return_out_of_range_message_when_user_gave_one_negative_number() {
        // given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(clock, ticketUUIDGenerator, ticketRepository);

        LocalDateTime drawDate = LocalDateTime.of(2023, 12, 16, 12, 0, 0);

        InputNumbersRequestDto inputNumbersRequest = new InputNumbersRequestDto(Set.of(79, 33, 21, 17, 19));

        when(ticketRepository.save(any(Ticket.class)))
                .thenReturn(Ticket.builder()
                        .ticketUUID("123456")
                        .inputNumbers(Set.of(79, 33, 21, 17, 19))
                        .drawDate(drawDate)
                        .message(OUT_OF_RANGE_NUMBERS.getInfo())
                        .build());

        when(drawDateFacade.retrieveNextDrawDate()).thenReturn(drawDate);
        // when
        TicketResponseDto actualNumberReceiver = numberReceiverFacade.inputNumbers(inputNumbersRequest);
        // then
        assertThat(actualNumberReceiver.message()).isEqualTo(OUT_OF_RANGE_NUMBERS.getInfo());
    }

    @Test
    public void should_return_correct_ticketUUID() {
        //given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(clock, ticketUUIDGenerator, ticketRepository);

        InputNumbersRequestDto inputNumbersRequest = new InputNumbersRequestDto(Set.of(-45, 79, -33, 21, 17, 19));
        //when

        when(ticketRepository.save(any(Ticket.class)))
                .thenReturn(new Ticket("123456", Set.of(1, 2, 3, 4, 5, 6), LocalDateTime.now(), EQUALS_SIX_NUMBERS.getInfo()));

        TicketResponseDto actualTicket = numberReceiverFacade.inputNumbers(inputNumbersRequest);
        String actualTicketId = actualTicket.ticketUUID();
        //then
        assertThat(actualTicket).isNotNull();
        assertThat(actualTicketId).isEqualTo("123456");
        assertThat(actualTicket.inputNumbers()).hasSize(6);
    }

    @Test
    public void should_return_correct_draw_date() {
        // given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(clock, ticketUUIDGenerator, ticketRepository);

        InputNumbersRequestDto inputNumbersRequestDto = new InputNumbersRequestDto(Set.of(1, 2, 3, 4, 5, 6));

        LocalDateTime expectedDrawDate = LocalDateTime.of(2023, 12, 16, 12, 0, 0);


        when(ticketRepository.save(any(Ticket.class)))
                .thenReturn(Ticket.builder()
                        .ticketUUID("123456")
                        .inputNumbers(Set.of(1,2,3,4,5,6))
                        .drawDate(expectedDrawDate)
                        .build());

        // when

        TicketResponseDto actualTicket = numberReceiverFacade.inputNumbers(inputNumbersRequestDto);
        LocalDateTime actualDrawDate = actualTicket.drawDate();
        // then
        assertThat(actualDrawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void should_return_next_Saturday_draw_date_when_date_is_Saturday_afternoon() {
        //given
        DrawDateFacade drawDateFacade = new DrawDateFacadeConfiguration()
                .drawDateFacade(clock);

        LocalDateTime expectedDrawDate = LocalDateTime.of(2023, 12, 16, 12, 0, 0, 0);
        //when
        LocalDateTime actualDrawDate = drawDateFacade.retrieveNextDrawDate();
        //then
        assertThat(actualDrawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void should_return_next_Saturday_draw_date_when_date_is_Saturday_noon() {
        //given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(clock, ticketUUIDGenerator, ticketRepository);

        when(ticketRepository.save(any(Ticket.class)))
                .thenReturn(new Ticket(UUID.randomUUID().toString(), Set.of(1, 2, 3, 4, 5, 6), LocalDateTime.of(2023, 11, 17, 12, 0, 0, 0), EQUALS_SIX_NUMBERS.getInfo()));

        //when
        TicketResponseDto ticketResult = numberReceiverFacade.inputNumbers(new InputNumbersRequestDto(Set.of(1, 2, 3, 4, 5, 6)));
        LocalDateTime actualDrawDate = ticketResult.drawDate();

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
                .ticketUUID("123456")
                .inputNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(LocalDateTime.of(2023, 12, 16, 12, 0, 0, 0))
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
                .numberReceiverFacade(clock, ticketUUIDGenerator, ticketRepository);
        DrawDateFacade drawDateFacade = new DrawDateFacadeConfiguration()
                .drawDateFacade(clock);
        //when
        List<TicketDto> actualTickets = numberReceiverFacade.retrieveTicketsByDrawDate(drawDateFacade.retrieveNextDrawDate());
        //then
        assertThat(actualTickets).isEqualTo(Collections.EMPTY_LIST);
    }

    @Test
    public void it_should_return_empty_collections_if_given_date_is_after_next_drawDate() {
        // given
        LocalDateTime actualDrawDate = LocalDateTime.of(2022, 10, 8, 12, 0, 0);
        AdjustableClock clock = new AdjustableClock(actualDrawDate.toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(clock, ticketUUIDGenerator, ticketRepository);

        LocalDateTime expectedDrawDate = LocalDateTime.of(2022, 10, 15, 12, 0, 0);
        // when
        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveTicketsByDrawDate(expectedDrawDate);
        // then
        assertThat(allTicketsByDate).isEmpty();
        assertThat(expectedDrawDate).isAfter(actualDrawDate);
    }

    @Test
    public void should_return_next_draw_date() {
        // given
        DrawDateFacade drawDateFacade = new DrawDateFacadeConfiguration()
                .drawDateFacade(clock);

        LocalDateTime expectedDrawDate = LocalDateTime.of(2023, 12, 16, 12, 0, 0, 0);
        // when
        LocalDateTime actualDrawDate = drawDateFacade.retrieveNextDrawDate();
        // then
        assertThat(actualDrawDate).isEqualTo(expectedDrawDate);
    }
}