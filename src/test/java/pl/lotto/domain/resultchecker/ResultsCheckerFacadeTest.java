package pl.lotto.domain.resultchecker;

import org.junit.jupiter.api.Test;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.numberreceiver.exceptions.WinningTicketNotFoundException;
import pl.lotto.domain.numbersgenerator.WinningTicketFacade;
import pl.lotto.domain.numbersgenerator.dto.WinningTicketResponseDto;
import pl.lotto.domain.resultchecker.dto.ResultCheckerResponseDto;
import pl.lotto.domain.resultchecker.exceptions.PlayerResultNotFoundException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.lotto.domain.numberreceiver.TicketResultMessage.TICKET_LOSE;
import static pl.lotto.domain.numberreceiver.TicketResultMessage.TICKET_WIN;
import static pl.lotto.domain.resultchecker.ResultCheckerFacadeTestConstant.LOSE;
import static pl.lotto.domain.resultchecker.ResultCheckerMessageProvider.PLAYER_WIN;
import static pl.lotto.domain.resultchecker.ResultCheckerMessageProvider.WIN;

class ResultsCheckerFacadeTest {

    WinningTicketFacade winningTicketFacade = mock(WinningTicketFacade.class);
    NumberReceiverFacade numberReceiverFacade = mock(NumberReceiverFacade.class);
    DrawDateFacade drawDateFacade = mock(DrawDateFacade.class);
    ResultCheckerRepository resultCheckerRepository = mock(ResultCheckerRepository.class);


    @Test
    public void should_generate_one_player_win() throws Exception {
        //given
        ResultsCheckerFacade resultCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .resultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningTicketFacade, resultCheckerRepository);

        LocalDateTime drawDate = LocalDateTime.of(2023, 11, 25, 12, 0, 0);
        String ticketUUID1 = "550e8125-e29b-41d4-a716-446655440000";
        String ticketUUID2 = "550e8547-e29b-41d4-a716-446655440000";

        when(drawDateFacade.retrieveNextDrawDate()).thenReturn(drawDate);

        when(winningTicketFacade.generateWinningTicket()).thenReturn(
                WinningTicketResponseDto.builder()
                        .ticketUUID(ticketUUID1)
                        .winningNumbers(Set.of(3, 4, 5))
                        .drawDate(drawDate)
                        .isWinner(true)
                        .message(TICKET_WIN.getMessage())
                        .build());
        when(numberReceiverFacade.retrieveTicketsByDrawDate(drawDate)).thenReturn(
                List.of(
                        TicketDto.builder()
                                .ticketUUID(ticketUUID1)
                                .inputNumbers(Set.of(1, 2, 3, 4, 5, 6))
                                .hitNumbers(Set.of(3, 4, 5))
                                .drawDate(drawDate)
                                .isWinner(true)
                                .message(TICKET_WIN.getMessage())
                                .build(),
                        TicketDto.builder()
                                .ticketUUID(ticketUUID2)
                                .inputNumbers(Set.of(1, 2, 3, 4, 5, 6))
                                .hitNumbers(Set.of(11, 12, 14))
                                .drawDate(drawDate)
                                .isWinner(false)
                                .message(TICKET_LOSE.getMessage())
                                .build()
                ));
        //when
        ResultCheckerResponseDto playerResult = resultCheckerFacade.generateResults();
        //then
        assertThat(playerResult).isNotNull();
    }

    @Test
    public void should_throwing_player_result_not_found_exception_when_winning_numbers_is_empty() {
        //given
        ResultsCheckerFacade resultCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .resultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningTicketFacade, resultCheckerRepository);

        when(winningTicketFacade.generateWinningTicket()).thenReturn(
                WinningTicketResponseDto.builder()
                        .winningNumbers(Set.of())
                        .build()
        );

        //when and then
        assertThatThrownBy(resultCheckerFacade::generateResults)
                .isInstanceOf(PlayerResultNotFoundException.class);
    }

    @Test
    public void should_generate_lose_message_when_no_winning_numbers() throws WinningTicketNotFoundException {
        //given
        when(winningTicketFacade.generateWinningTicket()).thenReturn(
                WinningTicketResponseDto.builder()
                        .ticketUUID("564e8400-e29b-41d4-a716-446655440000")
                        .drawDate(LocalDateTime.of(2021, 12, 11, 12, 0, 0, 0))
                        .winningNumbers(Collections.emptySet())
                        .message(LOSE)
                        .build()
        );
        //when
        WinningTicketResponseDto ticket = winningTicketFacade.generateWinningTicket();
        //then
        assertThat(ticket.message()).isEqualTo(LOSE);
    }

    @Test
    public void should_return_result_with_correct_credentials() throws Exception {
        //given
        ResultsCheckerFacade resultCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .resultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningTicketFacade, resultCheckerRepository);

        LocalDateTime drawDate = LocalDateTime.of(2023, 12, 25, 12, 0, 0);
        String ticketUUID1 = "550e8400-e29b-41d4-a716-446655440000";
        String ticketUUID2 = "560e8400-e29b-41d4-a716-446655440007";

        TicketDto ticket1 = TicketDto.builder()
                .ticketUUID(ticketUUID1)
                .inputNumbers(Set.of(4, 5, 6, 10, 11, 12))
                .hitNumbers(Set.of(4, 5, 6, 10))
                .drawDate(drawDate)
                .message(TICKET_WIN.getMessage())
                .build();

        TicketDto ticket2 = TicketDto.builder()
                .ticketUUID(ticketUUID2)
                .inputNumbers(Set.of(4, 5, 6, 10, 11, 12))
                .hitNumbers(Set.of(4, 5, 6))
                .drawDate(drawDate)
                .message(TICKET_LOSE.getMessage())
                .build();

        ResultCheckerResponse resultCheckerResponse1 = ResultCheckerResponse.builder()
                .ticketUUID(ticketUUID1)
                .inputNumbers(Set.of(4, 5, 6, 10, 11, 12))
                .hitNumbers(Set.of(4, 5, 6, 10))
                .drawDate(drawDate)
                .isWinner(true)
                .message(WIN)
                .build();

        ResultCheckerResponse resultCheckerResponse2 = ResultCheckerResponse.builder()
                .ticketUUID(ticketUUID2)
                .inputNumbers(Set.of(4, 5, 6, 10, 11, 12))
                .hitNumbers(Set.of(4, 5, 6))
                .drawDate(drawDate)
                .isWinner(false)
                .build();

        when(drawDateFacade.retrieveNextDrawDate()).thenReturn(drawDate);

        when(winningTicketFacade.generateWinningTicket()).thenReturn(
                WinningTicketResponseDto.builder()
                        .ticketUUID(ticketUUID1)
                        .winningNumbers(Set.of(4, 5, 6, 10))
                        .drawDate(drawDate)
                        .isWinner(true)
                        .build());

        when(numberReceiverFacade.retrieveTicketsByDrawDate(drawDate))
                .thenReturn(List.of(ticket1, ticket2));

        when(resultCheckerRepository.saveAll(anyList())).thenReturn(List.of(resultCheckerResponse1, resultCheckerResponse2));
        //when

        ResultDto actualResult = resultCheckerFacade.findResultByTicketUUID(ticketUUID1);
        //then
        ResultDto expectedResult = ResultDto.builder()
                .ticketUUID(ticketUUID1)
                .numbers(Set.of(4, 5, 6, 10, 11, 12))
                .hitNumbers(Set.of(4, 5, 6, 10))
                .drawDate(drawDate)
                .isWinner(true)
                .message(PLAYER_WIN)
                .build();

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void should_return_win_player_when_hit_numbers_are_more_than_three() {
        //given
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .resultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningTicketFacade, resultCheckerRepository);

        TicketPlayer ticketPlayer1 = TicketPlayer.builder()
                .ticketUUID("1234")
                .numbers(Set.of(1, 2, 3, 4, 11, 17, 19))
                .hitNumbers(Set.of(4, 11, 19))
                .drawDate(LocalDateTime.of(2023, 12, 25, 12, 0, 0))
                .build();

        TicketPlayer ticketPlayer2 = TicketPlayer.builder()
                .ticketUUID("5678")
                .numbers(Set.of(1, 2, 3, 4, 20, 35, 45))
                .hitNumbers(Set.of(1, 2))
                .drawDate(LocalDateTime.of(2023, 12, 25, 12, 0, 0))
                .build();

        TicketPlayer ticketPlayer3 = TicketPlayer.builder()
                .ticketUUID("9844")
                .numbers(Set.of(1, 2, 3, 4, 20, 35, 45))
                .hitNumbers(Set.of(20, 35, 45, 2))
                .isWinner(true)
                .message(TICKET_WIN.getMessage())
                .drawDate(LocalDateTime.of(2023, 12, 25, 12, 0, 0))
                .build();

        List<TicketPlayer> allTicketsByDate = List.of(ticketPlayer1, ticketPlayer2, ticketPlayer3);
        Set<Integer> winningNumbers = Set.of(20, 35, 45, 2);

        //when
        WinnersRetriever winnersRetriever = resultsCheckerFacade.winnersRetriever();
        List<ResultCheckerResponse> resultCheckerResponses = winnersRetriever.retrieveWinners(allTicketsByDate, winningNumbers);

        ResultCheckerResponse resultCheckerResponse1 = resultCheckerResponses.get(0);
        ResultCheckerResponse resultCheckerResponse2 = resultCheckerResponses.get(1);
        ResultCheckerResponse resultCheckerResponse3 = resultCheckerResponses.get(2);
        //then
        assertEquals(3, resultCheckerResponses.size());
        assertFalse(resultCheckerResponse1.isWinner());
        assertFalse(resultCheckerResponse2.isWinner());
        assertTrue(resultCheckerResponse3.isWinner());
    }

    @Test
    public void should_return_not_win_player_when_hit_numbers_are_less_than_three() {
        //given
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .resultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningTicketFacade, resultCheckerRepository);

        TicketPlayer ticketPlayer1 = TicketPlayer.builder()
                .ticketUUID("1234")
                .numbers(Set.of(1, 2, 9, 10, 11, 17, 19))
                .drawDate(LocalDateTime.of(2022, 4, 8, 12, 0, 0))
                .build();

        TicketPlayer ticketPlayer2 = TicketPlayer.builder()
                .ticketUUID("5678")
                .numbers(Set.of(1, 3, 22, 8, 20, 35, 45))
                .drawDate(LocalDateTime.of(2022, 4, 8, 12, 0, 0))
                .build();

        TicketPlayer ticketPlayer3 = TicketPlayer.builder()
                .ticketUUID("1111")
                .numbers(Set.of(1, 6, 14, 20, 90, 30, 8))
                .drawDate(LocalDateTime.of(2022, 4, 8, 12, 0, 0))
                .build();

        List<TicketPlayer> allTicketsByDate = List.of(ticketPlayer1, ticketPlayer2, ticketPlayer3);
        Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5, 6, 7);

        //when
        WinnersRetriever winnersRetriever = resultsCheckerFacade.winnersRetriever();
        List<ResultCheckerResponse> resultCheckerResponses = winnersRetriever.retrieveWinners(allTicketsByDate, winningNumbers);

        ResultCheckerResponse resultCheckerResponse1 = resultCheckerResponses.get(0);
        ResultCheckerResponse resultCheckerResponse2 = resultCheckerResponses.get(1);
        ResultCheckerResponse resultCheckerResponse3 = resultCheckerResponses.get(2);
        //then
        assertThat(resultCheckerResponses).isNotNull();
        assertFalse(resultCheckerResponse1.isWinner());
        assertFalse(resultCheckerResponse2.isWinner());
        assertFalse(resultCheckerResponse3.isWinner());

        assertThat(resultCheckerResponse1.hitNumbers().size()).isEqualTo(2);
        assertThat(resultCheckerResponse2.hitNumbers().size()).isEqualTo(2);
        assertThat(resultCheckerResponse3.hitNumbers().size()).isEqualTo(2);
    }

    @Test
    public void should_return_not_win_player_when_hit_numbers_are_empty() {
        //given
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .resultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningTicketFacade, resultCheckerRepository);

        TicketPlayer ticketPlayer1 = TicketPlayer.builder()
                .ticketUUID("1234")
                .numbers(Set.of(22, 45, 9, 10, 11, 17, 19))
                .drawDate(LocalDateTime.of(2022, 4, 8, 12, 0, 0))
                .build();

        TicketPlayer ticketPlayer2 = TicketPlayer.builder()
                .ticketUUID("5678")
                .numbers(Set.of(40, 17, 22, 12, 20, 35, 45))
                .drawDate(LocalDateTime.of(2022, 4, 8, 12, 0, 0))
                .build();

        TicketPlayer ticketPlayer3 = TicketPlayer.builder()
                .ticketUUID("1111")
                .numbers(Set.of(60, 45, 14, 20, 90, 30, 8))
                .drawDate(LocalDateTime.of(2022, 4, 8, 12, 0, 0))
                .build();

        List<TicketPlayer> allTicketsByDate = List.of(ticketPlayer1, ticketPlayer2, ticketPlayer3);
        Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5, 6, 7);

        //when
        WinnersRetriever winnersRetriever = resultsCheckerFacade.winnersRetriever();
        List<ResultCheckerResponse> resultCheckerResponses = winnersRetriever.retrieveWinners(allTicketsByDate, winningNumbers);

        ResultCheckerResponse resultCheckerResponse1 = resultCheckerResponses.get(0);
        ResultCheckerResponse resultCheckerResponse2 = resultCheckerResponses.get(1);
        ResultCheckerResponse resultCheckerResponse3 = resultCheckerResponses.get(2);
        //then
        assertThat(resultCheckerResponses).isNotNull();
        assertFalse(resultCheckerResponse1.isWinner());
        assertFalse(resultCheckerResponse2.isWinner());
        assertFalse(resultCheckerResponse3.isWinner());

        assertThat(resultCheckerResponse1.hitNumbers().size()).isEqualTo(0);
        assertThat(resultCheckerResponse2.hitNumbers().size()).isEqualTo(0);
        assertThat(resultCheckerResponse3.hitNumbers().size()).isEqualTo(0);
    }

    @Test
    public void should_return_not_win_player_when_input_numbers_are_empty() {
        //given
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .resultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningTicketFacade, resultCheckerRepository);

        TicketPlayer ticketPlayer1 = TicketPlayer.builder()
                .ticketUUID("1234")
                .numbers(Set.of())
                .drawDate(LocalDateTime.of(2022, 4, 8, 12, 0, 0))
                .build();

        TicketPlayer ticketPlayer2 = TicketPlayer.builder()
                .ticketUUID("5678")
                .numbers(Set.of())
                .drawDate(LocalDateTime.of(2022, 4, 8, 12, 0, 0))
                .build();

        TicketPlayer ticketPlayer3 = TicketPlayer.builder()
                .ticketUUID("1111")
                .numbers(Set.of())
                .drawDate(LocalDateTime.of(2022, 4, 8, 12, 0, 0))
                .build();

        List<TicketPlayer> allTicketsByDate = List.of(ticketPlayer1, ticketPlayer2, ticketPlayer3);
        Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5, 6, 7);

        //when
        WinnersRetriever winnersRetriever = resultsCheckerFacade.winnersRetriever();
        List<ResultCheckerResponse> resultCheckerResponses = winnersRetriever.retrieveWinners(allTicketsByDate, winningNumbers);

        ResultCheckerResponse resultCheckerResponse1 = resultCheckerResponses.get(0);
        ResultCheckerResponse resultCheckerResponse2 = resultCheckerResponses.get(1);
        ResultCheckerResponse resultCheckerResponse3 = resultCheckerResponses.get(2);
        //then
        assertThat(resultCheckerResponses).isNotNull();
        assertFalse(resultCheckerResponse1.isWinner());
        assertFalse(resultCheckerResponse2.isWinner());
        assertFalse(resultCheckerResponse3.isWinner());

        assertThat(resultCheckerResponse1.hitNumbers().size()).isEqualTo(0);
        assertThat(resultCheckerResponse2.hitNumbers().size()).isEqualTo(0);
        assertThat(resultCheckerResponse3.hitNumbers().size()).isEqualTo(0);
    }

    @Test
    public void should_return_empty_players_when_tickets_is_empty() {
        //given
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .resultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningTicketFacade, resultCheckerRepository);

        Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5, 6, 7);
        //when
        WinnersRetriever winnersRetriever = resultsCheckerFacade.winnersRetriever();
        List<ResultCheckerResponse> resultCheckerResponses = winnersRetriever.retrieveWinners(Collections.emptyList(), winningNumbers);
        //then
        assertTrue(resultCheckerResponses.isEmpty());
    }

    @Test
    public void should_return_no_winners_when_tickets_is_empty_and_winning_numbers_is_null() {
        //given
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .resultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningTicketFacade, resultCheckerRepository);

        //when
        WinnersRetriever winnersRetriever = resultsCheckerFacade.winnersRetriever();

        List<ResultCheckerResponse> resultCheckerResponses = winnersRetriever.retrieveWinners(Collections.emptyList(), null);
        //then
        assertThat(resultCheckerResponses).isNullOrEmpty();
    }
}
