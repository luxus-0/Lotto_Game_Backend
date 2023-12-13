package pl.lotto.domain.resultannouncer;

import org.junit.jupiter.api.Test;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.resultannouncer.dto.ResultAnnouncerResponseDto;
import pl.lotto.domain.resultannouncer.exceptions.ResultAnnouncerNotFoundException;
import pl.lotto.domain.resultannouncer.exceptions.TicketUUIDNotFoundException;
import pl.lotto.domain.resultchecker.ResultsCheckerFacade;
import pl.lotto.domain.resultchecker.dto.ResultResponseDto;
import pl.lotto.domain.resultchecker.exceptions.ResultNotFoundException;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.lotto.domain.resultannouncer.ResultStatus.*;

class ResultAnnouncerFacadeTest {

    ResultsCheckerFacade resultsCheckerFacade = mock(ResultsCheckerFacade.class);
    ResultAnnouncerRepository resultAnnouncerRepository = mock(ResultAnnouncerRepository.class);
    DrawDateFacade drawDateFacade = mock(DrawDateFacade.class);

    Clock clock = Clock.fixed(LocalDateTime.of(2023, 12, 12, 12, 0, 0, 0).toInstant(UTC), ZoneId.systemDefault());

    @Test
    public void should_return_lose_message_when_ticket_is_not_winning_ticket() throws Exception {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2022, 12, 12, 12, 0, 0);
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerFacadeConfiguration()
                .resultAnnouncerFacade(resultsCheckerFacade, resultAnnouncerRepository, clock);

        String ticketUUID = "123456";
        ResultResponseDto expectedResult = ResultResponseDto.builder()
                .ticketUUID(ticketUUID)
                .inputNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of())
                .drawDate(drawDate)
                .build();

        ResultAnnouncerResponse expectedResultAnnouncerResponse = ResultAnnouncerResponse.builder()
                .ticketUUID(ticketUUID)
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of())
                .isWinner(false)
                .drawDate(drawDate)
                .message(LOSE.message)
                .build();

        when(drawDateFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.of(2022, 12, 11, 12, 12, 0, 0));
        when(resultsCheckerFacade.findResultByTicketUUID(ticketUUID)).thenReturn(expectedResult);
        when(resultAnnouncerRepository.findAllByTicketUUID(ticketUUID)).thenReturn(Optional.of(expectedResultAnnouncerResponse));
        when(resultAnnouncerRepository.save(any(ResultAnnouncerResponse.class))).thenReturn(expectedResultAnnouncerResponse);
        //when && then
        ResultAnnouncerResponseDto actualResult = resultAnnouncerFacade.findResult(ticketUUID);

        assertThat(actualResult.message()).isEqualTo(LOSE.message);
    }

    @Test
    public void should_return_win_message_when_ticket_is_winning_ticket() throws Exception {
        //given
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerFacadeConfiguration()
                .resultAnnouncerFacade(resultsCheckerFacade, resultAnnouncerRepository, clock);
        LocalDateTime drawDate = LocalDateTime.of(2023, 12, 13, 12, 0, 0);
        String ticketUUID = "123456";

        ResultResponseDto expectedResult = ResultResponseDto.builder()
                .ticketUUID(ticketUUID)
                .inputNumbers(Set.of(3, 4, 5, 6, 7, 8))
                .hitNumbers(Set.of(3, 4, 5 ,6))
                .drawDate(drawDate)
                .isWinner(true)
                .message(WIN.message)
                .build();

        ResultAnnouncerResponse expectedResultAnnouncerResponse = ResultAnnouncerResponse.builder()
                .ticketUUID(ticketUUID)
                .numbers(Set.of(3, 4, 5, 6, 7, 8))
                .hitNumbers(Set.of(3, 4, 5, 6))
                .isWinner(true)
                .drawDate(drawDate)
                .message(WIN.message)
                .build();

        ResultAnnouncerResponseDto expectedResults = ResultAnnouncerResponseDto.builder()
                .ticketUUID(ticketUUID)
                .inputNumbers(Set.of(3, 4, 5, 6, 7, 8))
                .hitNumbers(Set.of(3, 4, 5, 6))
                .isWinner(true)
                .drawDate(drawDate)
                .message(WIN.message)
                .build();

        when(resultsCheckerFacade.findResultByTicketUUID(ticketUUID))
                .thenReturn(expectedResult);

        when(resultAnnouncerRepository.findAllByTicketUUID(ticketUUID))
                .thenReturn(Optional.of(expectedResultAnnouncerResponse));

        when(resultAnnouncerRepository.save(any(ResultAnnouncerResponse.class)))
                .thenReturn(expectedResultAnnouncerResponse);
        //when
        ResultAnnouncerResponseDto actualResult = resultAnnouncerFacade.findResult(ticketUUID);
        //then

        assertThat(actualResult).isEqualTo(expectedResults);
        assertThat(actualResult.message()).isEqualTo(WAIT.message);
    }

    @Test
    public void should_return_wait_message_when_date_is_before_announcement_time() throws Exception {
        //given
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerFacadeConfiguration()
                .resultAnnouncerFacade(resultsCheckerFacade, resultAnnouncerRepository, clock);

        LocalDateTime drawDate = LocalDateTime.now().plusDays(2);
        String ticketUUID = "123456";

        ResultAnnouncerResponse resultAnnouncerResponse = ResultAnnouncerResponse.builder()
                .ticketUUID(ticketUUID)
                .numbers(Set.of(4, 7, 9, 11, 13, 15))
                .drawDate(drawDate)
                .build();

        ResultResponseDto result = ResultResponseDto.builder()
                .ticketUUID(ticketUUID)
                .inputNumbers(Set.of(4, 7, 9, 11, 13, 15))
                .drawDate(drawDate)
                .build();

        when(resultsCheckerFacade.findResultByTicketUUID(ticketUUID))
                .thenReturn(result);

        when(resultAnnouncerRepository.findAllByTicketUUID(ticketUUID))
                .thenReturn(Optional.of(resultAnnouncerResponse));

        when(resultAnnouncerRepository.save(any(ResultAnnouncerResponse.class)))
                .thenReturn(resultAnnouncerResponse);
        //when
        ResultAnnouncerResponseDto actualResult = resultAnnouncerFacade.findResult(ticketUUID);
        //then

        ResultAnnouncerResponseDto expectedResult = ResultAnnouncerResponseDto.builder()
                .ticketUUID(ticketUUID)
                .inputNumbers(Set.of(4, 7, 9, 11, 13, 15))
                .drawDate(LocalDateTime.of(2023, 12, 16, 12, 0, 0))
                .message(WAIT.message)
                .build();

        assertThat(actualResult.drawDate()).isBefore(expectedResult.drawDate());
    }

    @Test
    public void should_thrown_exception_when_ticketUUID_is_null() {
        //given
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerFacadeConfiguration()
                .resultAnnouncerFacade(resultsCheckerFacade, resultAnnouncerRepository, clock);

        //when && then
        assertThrowsExactly(TicketUUIDNotFoundException.class, () -> resultAnnouncerFacade.findResult(null));
    }

    @Test
    public void should_thrown_checked_exception_when_ticket_id_is_empty() throws ResultNotFoundException {
        //given
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerFacadeConfiguration()
                .resultAnnouncerFacade(resultsCheckerFacade, resultAnnouncerRepository, clock);
        String ticketUUID = "";
        LocalDateTime drawDate = LocalDateTime.now(clock).plusDays(2);

        ResultAnnouncerResponse expectedResult = ResultAnnouncerResponse.builder()
                .ticketUUID(ticketUUID)
                .numbers(Set.of(1, 45, 67, 76, 23, 48))
                .hitNumbers(Set.of(1, 76, 45))
                .drawDate(drawDate)
                .isWinner(true)
                .message(WIN.message)
                .build();

        ResultResponseDto resultDto = ResultResponseDto.builder()
                .ticketUUID(ticketUUID)
                .inputNumbers(Set.of(1, 45, 67, 76, 23, 48))
                .hitNumbers(Set.of(1, 76, 45))
                .drawDate(drawDate)
                .isWinner(true)
                .message(WIN.message)
                .build();

        when(resultAnnouncerRepository.findAllByTicketUUID(ticketUUID))
                .thenReturn(Optional.of(expectedResult));

        when(resultAnnouncerRepository.save(any(ResultAnnouncerResponse.class)))
                .thenReturn(expectedResult);

        when(resultsCheckerFacade.findResultByTicketUUID(ticketUUID))
                .thenReturn(resultDto);

        //when && then
        assertThrowsExactly(TicketUUIDNotFoundException.class,
                () -> resultAnnouncerFacade.findResult(ticketUUID));
    }

    @Test
    public void should_thrown_checked_exception_when_ticket_id_is_null() throws ResultNotFoundException {
        //given
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerFacadeConfiguration()
                .resultAnnouncerFacade(resultsCheckerFacade, resultAnnouncerRepository, clock);

        ResultAnnouncerResponse expectedResult = ResultAnnouncerResponse.builder()
                .ticketUUID(null)
                .build();

        ResultResponseDto resultDto = ResultResponseDto.builder()
                .ticketUUID(null)
                .build();

        when(resultAnnouncerRepository.findAllByTicketUUID(null))
                .thenReturn(Optional.of(expectedResult));

        when(resultAnnouncerRepository.save(any(ResultAnnouncerResponse.class)))
                .thenReturn(expectedResult);

        when(resultsCheckerFacade.findResultByTicketUUID(null))
                .thenReturn(resultDto);

        //when && then
        assertThrowsExactly(TicketUUIDNotFoundException.class,
                () -> resultAnnouncerFacade.findResult(null));
    }

    @Test
    public void should_throw_an_exception_when_input_numbers_is_empty() throws Exception {
        //given
        String ticketUUID = "1234";

        ResultResponseDto expectedResult = ResultResponseDto.builder()
                .ticketUUID(ticketUUID)
                .inputNumbers(Collections.emptySet())
                .hitNumbers(Set.of(1, 2, 3, 4))
                .drawDate(LocalDateTime.now())
                .build();

        when(resultsCheckerFacade.findResultByTicketUUID(ticketUUID)).thenReturn(expectedResult);

        //when
        Set<Integer> actualNumbers = resultsCheckerFacade.findResultByTicketUUID(ticketUUID).inputNumbers();
        //then
        assertThrows(IllegalArgumentException.class,
                () -> actualNumbers.stream()
                        .findAny()
                        .orElseThrow(() -> new IllegalArgumentException("Player not found")));
    }

    @Test
    public void should_throw_an_exception_when_hit_numbers_is_empty() throws Exception {
        //given
        String ticketUUID = "";

        ResultResponseDto resultDto = ResultResponseDto.builder()
                .ticketUUID(ticketUUID)
                .inputNumbers(Set.of())
                .build();
        when(resultsCheckerFacade.findResultByTicketUUID(ticketUUID)).thenReturn(resultDto);

        //when
        ResultResponseDto actualResult = resultsCheckerFacade.findResultByTicketUUID(ticketUUID);
        //then
        Set<Integer> numbersResult = actualResult.inputNumbers();

        assertThrows(IllegalArgumentException.class,
                () -> numbersResult.stream()
                        .findAny()
                        .orElseThrow(() -> new IllegalArgumentException("Player not found")));
    }
}