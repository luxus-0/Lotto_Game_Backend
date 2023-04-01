package pl.lotto.domain.resultannouncer;

import org.junit.jupiter.api.Test;
import pl.lotto.domain.resultannouncer.dto.ResultResponseDto;
import pl.lotto.domain.resultchecker.ResultsCheckerFacade;
import pl.lotto.domain.resultchecker.dto.ResultDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.lotto.domain.resultannouncer.ResultAnnouncerMessage.LOSE_MESSAGE;

class ResultAnnouncerFacadeTest {

    ResultsCheckerFacade resultsCheckerFacade = mock(ResultsCheckerFacade.class);
    ResultAnnouncerRepository resultAnnouncerRepository = new ResultAnnouncerTestImpl();

    @Test
    public void should_return_lose_message_if_ticket_is_not_winning_ticket() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2022, 12, 17, 12, 0, 0);
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerFacadeConfiguration().createModuleForTest(resultsCheckerFacade, resultAnnouncerRepository, Clock.systemUTC());
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
        ResultResponseDto expectedResult = new ResultResponseDto(expectedResultDto, LOSE_MESSAGE);
        assertThat(actualResult).isEqualTo(expectedResult);

    }

}