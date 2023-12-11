package pl.lotto.domain.resultannouncer;

import org.junit.jupiter.api.Test;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.resultannouncer.exceptions.ResultAnnouncerNotFoundException;
import pl.lotto.domain.resultchecker.ResultsCheckerFacade;
import pl.lotto.domain.resultchecker.exceptions.PlayerResultNotFoundException;

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
import static pl.lotto.domain.resultannouncer.ResultStatus.LOSE;
import static pl.lotto.domain.resultannouncer.ResultStatus.WAIT;

class ResultCheckerResponseAnnouncerFacadeTest {

    ResultsCheckerFacade resultsCheckerFacade = mock(ResultsCheckerFacade.class);
    ResultAnnouncerRepository resultAnnouncerRepository = mock(ResultAnnouncerRepository.class);
    DrawDateFacade drawDateFacade = mock(DrawDateFacade.class);

    Clock clock = Clock.fixed(LocalDateTime.of(2023, 11, 12, 12, 0, 0, 0).toInstant(UTC), ZoneId.systemDefault());

    @Test
    public void should_return_lose_message_when_ticket_is_not_winning_ticket() throws Exception {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2022, 12, 12, 12, 0, 0);
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerFacadeConfiguration()
                .resultAnnouncerFacade(resultsCheckerFacade, resultAnnouncerRepository, clock);

        String ticketUUID = "123456";
        ResultDto expectedResult = ResultDto.builder()
                .ticketUUID(ticketUUID)
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of())
                .drawDate(drawDate)
                .isWinner(false)
                .message(LOSE.message)
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
        when(resultAnnouncerRepository.findByTicketUUID(ticketUUID)).thenReturn(Optional.of(expectedResultAnnouncerResponse));
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
        LocalDateTime drawDate = LocalDateTime.of(2023, 11, 25, 12, 0, 0);
        String ticketUUID = "123456";

        ResultDto expectedResult = ResultDto.builder()
                .ticketUUID(ticketUUID)
                .numbers(Set.of(3, 4, 5, 6, 7, 8))
                .hitNumbers(Set.of(3, 4, 5))
                .drawDate(drawDate)
                .isWinner(true)
                .build();

        ResultAnnouncerResponse expectedResultAnnouncerResponse = ResultAnnouncerResponse.builder()
                .ticketUUID(ticketUUID)
                .numbers(Set.of(3, 4, 5, 6, 7, 8))
                .hitNumbers(Set.of(3, 4, 5))
                .isWinner(true)
                .drawDate(drawDate)
                .build();

        when(resultsCheckerFacade.findResultByTicketUUID(ticketUUID))
                .thenReturn(expectedResult);

        when(resultAnnouncerRepository.findByTicketUUID(ticketUUID))
                .thenReturn(Optional.of(expectedResultAnnouncerResponse));

        when(resultAnnouncerRepository.save(any(ResultAnnouncerResponse.class)))
                .thenReturn(expectedResultAnnouncerResponse);
        //when
        ResultDto actualResult = resultAnnouncerFacade.findResult(ticketUUID).resultDto();
        //then

        assertThat(actualResult).isEqualTo(expectedResult);
        assertThat(actualResult.message()).isEqualTo(expectedResult.message());
    }

    @Test
    public void should_return_wait_message_when_date_is_before_announcement_time() throws Exception {
        //given
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerFacadeConfiguration()
                .resultAnnouncerFacade(resultsCheckerFacade, resultAnnouncerRepository, clock);

        LocalDateTime drawDate = LocalDateTime.now().plusDays(2);

        ResultAnnouncerResponse resultAnnouncerResponse = ResultAnnouncerResponse.builder()
                .ticketUUID("12345")
                .numbers(Set.of(4, 7, 9, 11, 13, 15))
                .drawDate(drawDate)
                .build();

        ResultDto result = ResultDto.builder()
                .ticketUUID("12345")
                .numbers(Set.of(4, 7, 9, 11, 13, 15))
                .drawDate(drawDate)
                .build();

        when(resultsCheckerFacade.findResultByTicketUUID("12345"))
                .thenReturn(result);

        when(resultAnnouncerRepository.findByTicketUUID("12345"))
                .thenReturn(Optional.of(resultAnnouncerResponse));

        when(resultAnnouncerRepository.save(any(ResultAnnouncerResponse.class)))
                .thenReturn(resultAnnouncerResponse);
        //when
        ResultAnnouncerResponseDto actualResult = resultAnnouncerFacade.findResult("12345");
        //then
        ResultDto responseDto = ResultDto.builder()
                .ticketUUID("12345")
                .numbers(Set.of(4, 7, 9, 11, 13, 15))
                .drawDate(drawDate)
                .build();

        ResultAnnouncerResponseDto expectedResult = new ResultAnnouncerResponseDto(responseDto, WAIT.message);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void should_thrown_exception_when_ticket_id_is_null() {
        //given
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerFacadeConfiguration()
                .resultAnnouncerFacade(resultsCheckerFacade, resultAnnouncerRepository, clock);

        //when && then
        assertThrowsExactly(IllegalArgumentException.class, () -> resultAnnouncerFacade.findResult(null));
    }

    @Test
    public void should_thrown_checked_exception_when_ticket_id_is_empty() {
        //given
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerFacadeConfiguration()
                .resultAnnouncerFacade(resultsCheckerFacade, resultAnnouncerRepository, clock);
        String ticketId = "";

        ResultAnnouncerResponse expectedResult = ResultAnnouncerResponse.builder()
                .ticketUUID("12345")
                .numbers(Set.of(1, 45, 67, 76, 23, 48))
                .hitNumbers(Set.of(1, 76, 45))
                .message("WIN")
                .isWinner(true)
                .build();

        when(resultAnnouncerRepository.findByTicketUUID(""))
                .thenThrow(ResultAnnouncerNotFoundException.class);

        when(resultAnnouncerRepository.save(any(ResultAnnouncerResponse.class)))
                .thenReturn(expectedResult);

        //when && then
        assertThrowsExactly(ResultAnnouncerNotFoundException.class,
                () -> resultAnnouncerFacade.findResult(ticketId));
    }

    @Test
    public void should_throw_an_exception_when_input_numbers_is_empty() throws Exception {
        //given
        String ticketUUID = "1234";

        ResultDto expectedResult = ResultDto.builder()
                .ticketUUID(ticketUUID)
                .numbers(Collections.emptySet())
                .hitNumbers(Set.of(1, 2, 3, 4))
                .drawDate(LocalDateTime.now())
                .build();

        when(resultsCheckerFacade.findResultByTicketUUID(ticketUUID)).thenReturn(expectedResult);

        //when
        Set<Integer> actualNumbers = resultsCheckerFacade.findResultByTicketUUID(ticketUUID).numbers();
        //then
        assertThrows(PlayerResultNotFoundException.class,
                () -> actualNumbers.stream()
                        .findAny()
                        .orElseThrow(() -> new PlayerResultNotFoundException("Player not found")));
    }

    @Test
    public void should_throw_an_exception_when_hit_numbers_is_empty() throws Exception {
        //given
        String ticketUUID = "";

        ResultDto resultDto = ResultDto.builder()
                .ticketUUID(ticketUUID)
                .numbers(Set.of())
                .build();
        when(resultsCheckerFacade.findResultByTicketUUID(ticketUUID)).thenReturn(resultDto);

        //when
        ResultDto actualResult = resultsCheckerFacade.findResultByTicketUUID(ticketUUID);
        //then
        Set<Integer> numbersResult = actualResult.numbers();

        assertThrows(PlayerResultNotFoundException.class,
                () -> numbersResult.stream()
                        .findAny()
                        .orElseThrow(() -> new PlayerResultNotFoundException("Player not found")));
    }
}