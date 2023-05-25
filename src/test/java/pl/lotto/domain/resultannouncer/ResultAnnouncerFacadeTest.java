package pl.lotto.domain.resultannouncer;

import org.junit.jupiter.api.Test;
import pl.lotto.domain.resultannouncer.dto.ResultAnnouncerResponseDto;
import pl.lotto.domain.resultchecker.ResultsCheckerFacade;
import pl.lotto.domain.resultchecker.dto.ResultDto;
import pl.lotto.domain.resultchecker.exceptions.PlayerResultNotFoundException;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Set;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.lotto.domain.resultannouncer.ResultStatus.*;

class ResultAnnouncerFacadeTest {

    ResultsCheckerFacade resultsCheckerFacade = mock(ResultsCheckerFacade.class);

    ResultLottoRepository resultLottoRepository = new InMemoryResultLottoRepository();

    Clock clock = Clock.fixed(LocalDateTime.of(2022, 12, 17, 12, 0,0).toInstant(UTC), ZoneId.systemDefault());
    ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerFacadeConfiguration().resultAnnouncerFacade(resultsCheckerFacade, resultLottoRepository, clock);

    @Test
    public void should_return_lose_message_when_ticket_is_not_winning_ticket() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2022, 12, 17, 12, 0, 0);
        String ticketId = "123456";
        ResultDto resultDto = ResultDto.builder()
                .ticketId(ticketId)
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of())
                .drawDate(drawDate)
                .isWinner(false)
                .build();
        when(resultsCheckerFacade.findResultByTicketId(ticketId)).thenReturn(resultDto);
        //when
        ResultAnnouncerResponseDto actualResult = resultAnnouncerFacade.findResult(ticketId);
        //then
        ResultDto expectedResultDto = ResultDto.builder()
                .ticketId(ticketId)
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of())
                .drawDate(drawDate)
                .isWinner(false)
                .build();
        ResultAnnouncerResponseDto expectedResult = new ResultAnnouncerResponseDto(expectedResultDto, LOSE.message);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void should_return_win_message_when_ticket_is_winning_ticket() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2022, 12, 17, 12, 0, 0);
        String ticketId = "13579";
        ResultDto resultDto = ResultDto.builder()
                .ticketId(ticketId)
                .numbers(Set.of(3, 4, 5, 6, 7, 8))
                .hitNumbers(Set.of(3,4,5))
                .drawDate(drawDate)
                .isWinner(true)
                .build();
        when(resultsCheckerFacade.findResultByTicketId(ticketId)).thenReturn(resultDto);
        //when
        ResultAnnouncerResponseDto actualResult = resultAnnouncerFacade.findResult(ticketId);
        //then
        ResultDto expectedResultDto = ResultDto.builder()
                .ticketId(ticketId)
                .numbers(Set.of(3, 4, 5, 6, 7, 8))
                .hitNumbers(Set.of(3,4,5))
                .drawDate(drawDate)
                .isWinner(true)
                .build();
        ResultAnnouncerResponseDto expectedResult = new ResultAnnouncerResponseDto(expectedResultDto, WIN.message);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void should_return_wait_message_when_date_is_before_announcement_time(){
        //given
        LocalDateTime drawDate = LocalDateTime.of(2022, 12, 31, 12, 0,0 );
        String hash = "12345";

        ResultDto resultDto = ResultDto.builder()
                .ticketId("12345")
                .numbers(Set.of(4, 7, 9, 11, 13, 15))
                .hitNumbers(Set.of(11, 4, 7 ,9))
                .drawDate(drawDate)
                .isWinner(true)
                .build();

        when(resultsCheckerFacade.findResultByTicketId(hash)).thenReturn(resultDto);
        //when
        ResultAnnouncerResponseDto actualResultAnnouncerResponseDto = resultAnnouncerFacade.findResult(hash);
        //then
        ResultDto responseDto = ResultDto.builder()
                .ticketId("12345")
                .numbers(Set.of(4, 7, 9, 11, 13, 15))
                .hitNumbers(Set.of(11, 4, 7, 9))
                .drawDate(drawDate)
                .isWinner(true)
                .build();

        ResultAnnouncerResponseDto expectedResultAnnouncerResponseDto = new ResultAnnouncerResponseDto(responseDto, WAIT.message);
        assertThat(actualResultAnnouncerResponseDto).isEqualTo(expectedResultAnnouncerResponseDto);
    }

    @Test
    public void should_return_hash_does_not_exist_message_when_hash_does_not_exist() {
        //given
        String ticketId = "";

        when(resultsCheckerFacade.findResultByTicketId(ticketId)).thenReturn(null);
        //when
        ResultAnnouncerResponseDto actualResultAnnouncerResponseDto = resultAnnouncerFacade.findResult(ticketId);
        //then
        ResultAnnouncerResponseDto expectedResultAnnouncerResponseDto = new ResultAnnouncerResponseDto(null, TICKET_ID_IS_EMPTY.message);
        assertThat(actualResultAnnouncerResponseDto).isEqualTo(expectedResultAnnouncerResponseDto);
    }

    @Test
    public void it_should_return_response_with_hash_does_not_exist_message_if_response_is_not_saved_to_db_yet() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2022, 12, 17, 12, 0, 0);
        String ticketId = "123";
        ResultDto resultDto = ResultDto.builder()
                .ticketId("123")
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3, 4, 9, 0))
                .drawDate(drawDate)
                .isWinner(true)
                .build();
        when(resultsCheckerFacade.findResultByTicketId(ticketId)).thenReturn(resultDto);

        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.findResult(ticketId);
        String resultByHash = resultAnnouncerResponseDto.resultDto().ticketId();
        //when
        ResultAnnouncerResponseDto actualResultDto = resultAnnouncerFacade.findResult(resultByHash);
        //then
        ResultAnnouncerResponseDto expectedResultDto = new ResultAnnouncerResponseDto(actualResultDto.resultDto(), ALREADY_CHECKED.message);
        assertThat(actualResultDto).isEqualTo(expectedResultDto);
    }

    @Test
    public void should_throw_an_exception_when_numbers_is_empty() {
        //given
        String ticketId = "1234";

        ResultDto resultDto = ResultDto.builder()
                .ticketId(ticketId)
                .numbers(Collections.emptySet())
                .hitNumbers(Set.of(1,2,3,4))
                .drawDate(LocalDateTime.now())
                .build();
        when(resultsCheckerFacade.findResultByTicketId(ticketId)).thenReturn(resultDto);

        //when
        Set<Integer> actualNumbers = resultsCheckerFacade.findResultByTicketId(ticketId).numbers();
        //then
        assertThrows(PlayerResultNotFoundException.class,
                () -> actualNumbers.stream()
                        .findAny()
                        .orElseThrow(() -> new PlayerResultNotFoundException("Player not found")));
    }

    @Test
    public void should_throw_an_exception_when_hit_numbers_is_empty() {
        //given
        String ticketId = "";

        ResultDto resultDto = ResultDto.builder()
                .ticketId(ticketId)
                .numbers(Set.of())
                .build();
        when(resultsCheckerFacade.findResultByTicketId(ticketId)).thenReturn(resultDto);

        //when
        ResultDto actualResult = resultsCheckerFacade.findResultByTicketId(ticketId);
        //then
        Set<Integer> numbersResult = actualResult.numbers();

        assertThrows(PlayerResultNotFoundException.class,
                () -> numbersResult.stream()
                        .findAny()
                        .orElseThrow(() -> new PlayerResultNotFoundException("Player not found")));
    }
}