package pl.lotto.domain.resultchecker;

import org.junit.jupiter.api.Test;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.numbersgenerator.WinningNumbersGeneratorFacade;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;
import pl.lotto.domain.resultchecker.dto.PlayersDto;
import pl.lotto.domain.resultchecker.dto.ResultDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResultsCheckerFacadeTest {

    private final PlayerRepository playerRepository = new PlayerRepositoryTestImpl();
    private final WinningNumbersGeneratorFacade winningNumbersGeneratorFacade = mock(WinningNumbersGeneratorFacade.class);
    private final NumberReceiverFacade numberReceiverFacade = mock(NumberReceiverFacade.class);

    private final DrawDateFacade drawDateFacade = mock(DrawDateFacade.class);
    private final ResultsCheckerFacade resultCheckerFacade = new ResultsCheckerFacadeConfiguration().createModuleForTests(numberReceiverFacade, drawDateFacade, winningNumbersGeneratorFacade, playerRepository);

    @Test
    public void it_should_generate_all_players_with_correct_message() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2022, 12, 17, 12, 0, 0);
        TicketDto ticket1 = getTicket1(drawDate);
        TicketDto ticket2 = getTicket2(drawDate);
        TicketDto ticket3 = getTicket3(drawDate);

        ResultDto result1 = getResult1(drawDate);
        ResultDto result2 = getResult2(drawDate);
        ResultDto result3 = getResult3(drawDate);

        when(winningNumbersGeneratorFacade.generateWinningNumbers()).thenReturn(getWinningNumbersDto());
        when(numberReceiverFacade.retrieveAllTicketByNextDrawDate()).thenReturn(List.of(ticket1, ticket2, ticket3));
        //when
        PlayersDto playersDto = resultCheckerFacade.generateWinners();
        //then
        List<ResultDto> results = playersDto.results();
        assertThat(results).contains(result1, result2, result3);
        String message = playersDto.message();
        assertThat(message).isEqualTo("Winners succeeded to retrieve");
    }

    static WinningNumbersDto getWinningNumbersDto() {
        return WinningNumbersDto.builder()
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .build();
    }

    static TicketDto getTicket3(LocalDateTime drawDate) {
        return TicketDto.builder()
                .hash("003")
                .numbers(Set.of(7, 8, 9, 10, 11, 12))
                .drawDate(drawDate)
                .build();
    }

    static TicketDto getTicket2(LocalDateTime drawDate) {
        return TicketDto.builder()
                .hash("002")
                .numbers(Set.of(1, 2, 7, 8, 9, 10))
                .drawDate(drawDate)
                .build();
    }

    static TicketDto getTicket1(LocalDateTime drawDate) {
        return TicketDto.builder()
                .hash("001")
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(drawDate)
                .build();
    }

    static ResultDto getResult3(LocalDateTime drawDate) {
        return ResultDto.builder()
                .hash("001")
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(drawDate)
                .isWinner(true)
                .build();
    }

    static ResultDto getResult2(LocalDateTime drawDate) {
        return ResultDto.builder()
                .hash("001")
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(drawDate)
                .isWinner(true)
                .build();
    }

    static ResultDto getResult1(LocalDateTime drawDate) {
        return ResultDto.builder()
                .hash("001")
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(drawDate)
                .isWinner(true)
                .build();
    }

}