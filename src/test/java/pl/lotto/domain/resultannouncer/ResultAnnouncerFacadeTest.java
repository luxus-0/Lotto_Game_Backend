package pl.lotto.domain.resultannouncer;

import org.junit.jupiter.api.Test;
import pl.lotto.domain.resultannouncer.dto.ResultResponseDto;
import pl.lotto.domain.resultchecker.ResultsCheckerFacade;
import pl.lotto.domain.resultchecker.dto.ResultDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.lotto.domain.resultannouncer.ResultStatus.*;

class ResultAnnouncerFacadeTest {

    ResultsCheckerFacade resultsCheckerFacade = mock(ResultsCheckerFacade.class);
    ResultLottoRepository resultLottoRepository = new ResultLottoTestImpl();

    Clock clock = Clock.fixed(LocalDateTime.of(2022, 12, 17, 12, 0,0).toInstant(UTC), ZoneId.systemDefault());
    ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerFacadeConfiguration().createModuleForTest(resultsCheckerFacade, resultLottoRepository, clock);

    @Test
    public void should_return_lose_message_when_ticket_is_not_winning_ticket() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2022, 12, 17, 12, 0, 0);
        String hash = "13579";
        ResultDto resultDto = ResultDto.builder()
                .hash(hash)
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of())
                .drawDate(drawDate)
                .isWinner(false)
                .build();
        when(resultsCheckerFacade.findByHash(hash)).thenReturn(resultDto);
        //when
        ResultResponseDto actualResult = resultAnnouncerFacade.findResult(hash);
        //then
        ResultDto expectedResultDto = ResultDto.builder()
                .hash(hash)
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of())
                .drawDate(drawDate)
                .isWinner(false)
                .build();
        ResultResponseDto expectedResult = new ResultResponseDto(expectedResultDto, LOSE.message);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void should_return_win_message_when_ticket_is_winning_ticket() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2022, 12, 17, 12, 0, 0);
        String hash = "13579";
        ResultDto resultDto = ResultDto.builder()
                .hash(hash)
                .numbers(Set.of(3, 4, 5, 6, 7, 8))
                .hitNumbers(Set.of(3,4,5))
                .drawDate(drawDate)
                .isWinner(true)
                .build();
        when(resultsCheckerFacade.findByHash(hash)).thenReturn(resultDto);
        //when
        ResultResponseDto actualResult = resultAnnouncerFacade.findResult(hash);
        //then
        ResultDto expectedResultDto = ResultDto.builder()
                .hash(hash)
                .numbers(Set.of(3, 4, 5, 6, 7, 8))
                .hitNumbers(Set.of(3,4,5))
                .drawDate(drawDate)
                .isWinner(true)
                .build();
        ResultResponseDto expectedResult = new ResultResponseDto(expectedResultDto, WIN.message);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void should_return_wait_message_when_date_is_before_announcement_time(){
        //given
        LocalDateTime drawDate = LocalDateTime.of(2022, 12, 31, 12, 0,0 );
        String hash = "12345";

        ResultDto resultDto = ResultDto.builder()
                .hash("12345")
                .numbers(Set.of(4, 7, 9, 11, 13, 15))
                .hitNumbers(Set.of(11, 4, 7 ,9))
                .drawDate(drawDate)
                .isWinner(true)
                .build();

        when(resultsCheckerFacade.findByHash(hash)).thenReturn(resultDto);
        //when
        ResultResponseDto actualResultResponseDto = resultAnnouncerFacade.findResult(hash);
        //then
        ResultDto responseDto = ResultDto.builder()
                .hash("12345")
                .numbers(Set.of(4, 7, 9, 11, 13, 15))
                .hitNumbers(Set.of(11, 4, 7 ,9))
                .drawDate(drawDate)
                .isWinner(true)
                .build();

        ResultResponseDto expectedResultResponseDto = new ResultResponseDto(responseDto, WAIT.message);
        assertThat(actualResultResponseDto).isEqualTo(expectedResultResponseDto);
    }

}