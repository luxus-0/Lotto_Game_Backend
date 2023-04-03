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

        when(winningNumbersGeneratorFacade.generateWinningNumbers()).thenReturn(
                WinningNumbersDto.builder()
                        .winningNumbers(Set.of(1, 3, 5, 7, 9, 11))
                        .build());
        when(numberReceiverFacade.retrieveAllTicketByNextDrawDate()).thenReturn(
                List.of(
                        TicketDto.builder()
                                .hash("001")
                                .numbers(Set.of(1, 3, 5, 7, 9, 11))
                                .drawDate(drawDate)
                                .build(),
                        TicketDto.builder()
                                .hash("002")
                                .numbers(Set.of(1, 3, 5, 7, 9, 11))
                                .drawDate(drawDate)
                                .build(),
                        TicketDto.builder()
                                .hash("003")
                                .numbers(Set.of(1, 3, 5, 7, 9, 11))
                                .drawDate(drawDate)
                                .build()
                )
        );
        //when
        PlayersDto playersDto = resultCheckerFacade.generateWinners();
        //then
        List<ResultDto> results = playersDto.results();
        ResultDto result1 = ResultDto.builder()
                .hash("001")
                .numbers(Set.of(1, 3, 5, 7, 9, 11))
                .hitNumbers(Set.of(1, 3, 5, 7, 9, 11))
                .drawDate(drawDate)
                .isWinner(true)
                .build();
        ResultDto result2 = ResultDto.builder()
                .hash("001")
                .numbers(Set.of(1, 3, 5, 7, 9, 11))
                .hitNumbers(Set.of(1, 3, 5, 7, 9, 11))
                .drawDate(drawDate)
                .isWinner(true)
                .build();
        ResultDto result3 = ResultDto.builder()
                .hash("003")
                .numbers(Set.of(1, 3, 5, 7, 9, 11))
                .hitNumbers(Set.of(1, 3, 5, 7, 9, 11))
                .drawDate(drawDate)
                .isWinner(true)
                .build();
        assertThat(results).contains(result1, result2, result3);
        String message = playersDto.message();
        assertThat(message).isEqualTo("Winners found");
    }

    @Test
    public void should_generate_fail_message_when_winning_numbers_is_empty() {
        //given
        when(winningNumbersGeneratorFacade.generateWinningNumbers()).thenReturn(
                WinningNumbersDto.builder()
                        .winningNumbers(Set.of())
                        .build()
        );
        //when
        PlayersDto playersDto = resultCheckerFacade.generateWinners();
        String message = playersDto.message();
        //then
        assertThat(message).isEqualTo("Winners not found");
    }

    @Test
    public void should_generate_fail_message_when_winning_numbers_equal_null() {
        //given
        when(winningNumbersGeneratorFacade.generateWinningNumbers()).thenReturn(
                WinningNumbersDto.builder()
                        .winningNumbers(null)
                        .build()
        );
        //when
        PlayersDto playersDto = resultCheckerFacade.generateWinners();
        String message = playersDto.message();
        //then
        assertThat(message).isEqualTo("Winners not found");
    }

    @Test
    public void should_generate_correct_message_when_winning_numbers_appear() {
        //given
        when(winningNumbersGeneratorFacade.generateWinningNumbers()).thenReturn(
                WinningNumbersDto.builder()
                        .winningNumbers(Set.of(200))
                        .build()
        );
        //when
        PlayersDto playersDto = resultCheckerFacade.generateWinners();
        String message = playersDto.message();
        //then
        assertThat(message).isEqualTo("Winners found");
    }

    @Test
    public void should_return_result_with_correct_credentials() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2022, 12, 17, 12, 0, 0);

        when(winningNumbersGeneratorFacade.generateWinningNumbers()).thenReturn(
                WinningNumbersDto.builder()
                        .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                        .build());
        String hash = "001";
        when(numberReceiverFacade.retrieveAllTicketByNextDrawDate()).thenReturn(
                List.of(
                        TicketDto.builder()
                                .hash(hash)
                                .numbers(Set.of(7, 8, 9, 10, 11, 12))
                                .drawDate(drawDate)
                                .build(),
                        TicketDto.builder()
                                .hash("002")
                                .numbers(Set.of(1, 3, 5, 7, 9, 11))
                                .drawDate(drawDate)
                                .build(),
                        TicketDto.builder()
                                .hash("003")
                                .numbers(Set.of(1, 3, 5, 7, 9, 11))
                                .drawDate(drawDate)
                                .build()
                )
        );
        resultCheckerFacade.generateWinners();
        //when
        ResultDto resultDto = resultCheckerFacade.findByHash(hash);

        //then
        ResultDto expectedResult = ResultDto.builder()
                .hash(hash)
                .numbers(Set.of(7, 8, 9, 10, 11, 12))
                .hitNumbers(Set.of())
                .drawDate(drawDate)
                .isWinner(false)
                .build();

        assertThat(resultDto).isNotNull();
        assertThat(resultDto).isEqualTo(expectedResult);
    }
}
