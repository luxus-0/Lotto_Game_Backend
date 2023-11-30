package pl.lotto.domain.resultchecker;

import org.junit.jupiter.api.Test;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.numbersgenerator.WinningTicketFacade;
import pl.lotto.domain.numbersgenerator.dto.WinningTicketDto;
import pl.lotto.domain.resultchecker.dto.PlayersDto;
import pl.lotto.domain.resultchecker.dto.ResultDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.lotto.domain.resultchecker.ResultCheckerFacadeTestConstant.LOSE;
import static pl.lotto.domain.resultchecker.ResultCheckerFacadeTestConstant.WIN;
import static pl.lotto.domain.resultchecker.ResultCheckerFacadeTestMessageProvider.getPlayerResultMessage;
import static pl.lotto.domain.resultchecker.ResultCheckerMessageProvider.PLAYER_LOSE;

class ResultsCheckerFacadeTest {

    WinningTicketFacade winningTicketFacade = mock(WinningTicketFacade.class);
    NumberReceiverFacade numberReceiverFacade = mock(NumberReceiverFacade.class);
    DrawDateFacade drawDateFacade = mock(DrawDateFacade.class);
    PlayerRepository playerRepository = mock(PlayerRepository.class);


    @Test
    public void should_generate_one_player_win() {
        //given
        ResultsCheckerFacade resultCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .resultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningTicketFacade, playerRepository);

        LocalDateTime drawDate = drawDateFacade.retrieveNextDrawDate();

        when(winningTicketFacade.generateWinningNumbers()).thenReturn(
                WinningTicketDto.builder()
                        .ticketId("123456")
                        .winningNumbers(Set.of(3, 4, 5))
                        .drawDate(drawDate)
                        .message(WIN)
                        .build());
        when(numberReceiverFacade.retrieveAllTicketByDrawDate(drawDate)).thenReturn(
                List.of(
                        TicketDto.builder()
                                .ticketId("123456")
                                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                                .hitNumbers(Set.of(3,4,5))
                                .drawDate(drawDate)
                                .message(WIN)
                                .build(),
                        TicketDto.builder()
                                .ticketId("123456")
                                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                                .hitNumbers(Set.of(11,12,14))
                                .drawDate(drawDate)
                                .message(LOSE)
                                .build()
                ));
        //when
        PlayersDto playerResult = resultCheckerFacade.generateResults();
        String playerResultMessage = getPlayerResultMessage(playerResult);
        //then
        assertThat(playerResultMessage).isEqualTo(WIN);
    }

    @Test
    public void should_generate_fail_message_when_winning_numbers_is_empty() {
        //given
        ResultsCheckerFacade resultCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .resultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningTicketFacade, playerRepository);

        when(winningTicketFacade.generateWinningNumbers()).thenReturn(
                WinningTicketDto.builder()
                        .winningNumbers(Set.of())
                        .build()
        );
        //when
        PlayersDto playersDto = resultCheckerFacade.generateResults();
        String playerResultMessage = getPlayerResultMessage(playersDto);
        //then
        assertThat(playerResultMessage).isEqualTo(PLAYER_LOSE);
    }

    @Test
    public void should_generate_lose_message_when_no_winning_numbers() {
        //given
        when(winningTicketFacade.generateWinningNumbers()).thenReturn(
                WinningTicketDto.builder()
                        .ticketId("123456")
                        .drawDate(LocalDateTime.of(2021, 12, 11, 12, 0,0,0))
                        .winningNumbers(Collections.emptySet())
                        .message(LOSE)
                        .build()
        );
        //when
        WinningTicketDto ticket = winningTicketFacade.generateWinningNumbers();
        //then
        assertThat(ticket.message()).isEqualTo(LOSE);
    }

    @Test
    public void should_return_result_with_correct_credentials() {
        //given
        ResultsCheckerFacade resultCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .resultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningTicketFacade, playerRepository);

        LocalDateTime drawDate = LocalDateTime.of(2023, 11, 11, 12, 0, 0);
        String ticketId = "001";

        when(drawDateFacade.retrieveNextDrawDate()).thenReturn(drawDate);

        when(winningTicketFacade.generateWinningNumbers()).thenReturn(
                WinningTicketDto.builder()
                        .ticketId(ticketId)
                        .winningNumbers(Set.of(4,5,6))
                        .drawDate(drawDate)
                        .message(WIN)
                .build());

        when(numberReceiverFacade.retrieveAllTicketByDrawDate(drawDate)).thenReturn(
                List.of(TicketDto.builder()
                                .ticketId(ticketId)
                                .numbers(Set.of(4, 5, 6, 10, 11, 12))
                                .hitNumbers(Set.of(4,5,6))
                                .drawDate(drawDate)
                                .isWinner(true)
                                .message(WIN)
                                .build(),
                        TicketDto.builder()
                                .ticketId("002")
                                .numbers(Set.of(4, 5, 6, 10, 11, 12))
                                .hitNumbers(Set.of(4,5,6))
                                .drawDate(drawDate)
                                .isWinner(true)
                                .message(WIN)
                                .build())
        );

    playerRepository.saveAll(List.of(Player.builder()
                        .ticketId(ticketId)
                        .numbers(Set.of(4, 5, 6, 10, 11, 12))
                        .hitNumbers(Set.of(4, 5, 6))
                        .drawDate(drawDate)
                        .isWinner(true)
                        .message(WIN)
                .build()));
        //when

        ResultDto actualResult = resultCheckerFacade.findResultByTicketId(ticketId);
        //then
        ResultDto expectedResult = ResultDto.builder()
                .ticketId(ticketId)
                .numbers(Set.of(4, 5, 6, 10, 11, 12))
                .hitNumbers(Set.of(4, 5, 6))
                .drawDate(drawDate)
                .isWinner(true)
                .message(WIN)
                .build();
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void should_return_win_player_when_hit_numbers_are_more_than_three() {
        //given
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .resultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningTicketFacade, playerRepository);

        Ticket ticket1 = Ticket.builder()
                .ticketId("1234")
                .numbers(Set.of(1, 2, 3, 4, 11, 17, 19))
                .drawDate(LocalDateTime.of(2022, 4, 8, 12, 0, 0)
                )
                .build();

        Ticket ticket2 = Ticket.builder()
                .ticketId("5678")
                .numbers(Set.of(1, 2, 3, 4, 20, 35, 45))
                .drawDate(LocalDateTime.of(2022, 4, 8, 12, 0, 0)
                )
                .build();

        Ticket ticket3 = Ticket.builder()
                .ticketId("1111")
                .numbers(Set.of(1, 2, 3, 4, 6, 7, 8))
                .drawDate(LocalDateTime.of(2022, 4, 8, 12, 0, 0))
                .build();

        List<Ticket> allTicketsByDate = List.of(ticket1, ticket2, ticket3);
        Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5, 6, 7);

        //when
        WinnersRetriever winnersRetriever = resultsCheckerFacade.winnersRetriever();
        List<Player> players = winnersRetriever.retrieveWinners(allTicketsByDate, winningNumbers);

        Player player1 = players.get(0);
        Player player2 = players.get(1);
        Player player3 = players.get(2);
        //then
        assertEquals(3, players.size());
        assertTrue(player1.isWinner());
        assertTrue(player2.isWinner());
        assertTrue(player3.isWinner());
    }

    @Test
    public void should_return_not_win_player_when_hit_numbers_are_less_than_three() {
        //given
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .resultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningTicketFacade, playerRepository);

        Ticket ticket1 = Ticket.builder()
                .ticketId("1234")
                .numbers(Set.of(1, 2, 9, 10, 11, 17, 19))
                .drawDate(LocalDateTime.of(2022, 4, 8, 12, 0, 0))
                .build();

        Ticket ticket2 = Ticket.builder()
                .ticketId("5678")
                .numbers(Set.of(1, 3, 22, 8, 20, 35, 45))
                .drawDate(LocalDateTime.of(2022, 4, 8, 12, 0, 0))
                .build();

        Ticket ticket3 = Ticket.builder()
                .ticketId("1111")
                .numbers(Set.of(1, 6, 14, 20, 90, 30, 8))
                .drawDate(LocalDateTime.of(2022, 4, 8, 12, 0, 0))
                .build();

        List<Ticket> allTicketsByDate = List.of(ticket1, ticket2, ticket3);
        Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5, 6, 7);

        //when
        WinnersRetriever winnersRetriever = resultsCheckerFacade.winnersRetriever();
        List<Player> players = winnersRetriever.retrieveWinners(allTicketsByDate, winningNumbers);

        Player player1 = players.get(0);
        Player player2 = players.get(1);
        Player player3 = players.get(2);
        //then
        assertThat(players).isNotNull();
        assertFalse(player1.isWinner());
        assertFalse(player2.isWinner());
        assertFalse(player3.isWinner());

        assertThat(player1.hitNumbers().size()).isEqualTo(2);
        assertThat(player2.hitNumbers().size()).isEqualTo(2);
        assertThat(player3.hitNumbers().size()).isEqualTo(2);
    }

    @Test
    public void should_return_not_win_player_when_hit_numbers_are_empty() {
        //given
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .resultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningTicketFacade, playerRepository);

        Ticket ticket1 = Ticket.builder()
                .ticketId("1234")
                .numbers(Set.of(22, 45, 9, 10, 11, 17, 19))
                .drawDate(LocalDateTime.of(2022, 4, 8, 12, 0, 0))
                .build();

        Ticket ticket2 = Ticket.builder()
                .ticketId("5678")
                .numbers(Set.of(40, 17, 22, 12, 20, 35, 45))
                .drawDate(LocalDateTime.of(2022, 4, 8, 12, 0, 0))
                .build();

        Ticket ticket3 = Ticket.builder()
                .ticketId("1111")
                .numbers(Set.of(60, 45, 14, 20, 90, 30, 8))
                .drawDate(LocalDateTime.of(2022, 4, 8, 12, 0, 0))
                .build();

        List<Ticket> allTicketsByDate = List.of(ticket1, ticket2, ticket3);
        Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5, 6, 7);

        //when
        WinnersRetriever winnersRetriever = resultsCheckerFacade.winnersRetriever();
        List<Player> players = winnersRetriever.retrieveWinners(allTicketsByDate, winningNumbers);

        Player player1 = players.get(0);
        Player player2 = players.get(1);
        Player player3 = players.get(2);
        //then
        assertThat(players).isNotNull();
        assertFalse(player1.isWinner());
        assertFalse(player2.isWinner());
        assertFalse(player3.isWinner());

        assertThat(player1.hitNumbers().size()).isEqualTo(0);
        assertThat(player2.hitNumbers().size()).isEqualTo(0);
        assertThat(player3.hitNumbers().size()).isEqualTo(0);
    }

    @Test
    public void should_return_not_win_player_when_input_numbers_are_empty() {
        //given
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .resultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningTicketFacade, playerRepository);

        Ticket ticket1 = Ticket.builder()
                .ticketId("1234")
                .numbers(Set.of())
                .drawDate(LocalDateTime.of(2022, 4, 8, 12, 0, 0))
                .build();

        Ticket ticket2 = Ticket.builder()
                .ticketId("5678")
                .numbers(Set.of())
                .drawDate(LocalDateTime.of(2022, 4, 8, 12, 0, 0))
                .build();

        Ticket ticket3 = Ticket.builder()
                .ticketId("1111")
                .numbers(Set.of())
                .drawDate(LocalDateTime.of(2022, 4, 8, 12, 0, 0))
                .build();

        List<Ticket> allTicketsByDate = List.of(ticket1, ticket2, ticket3);
        Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5, 6, 7);

        //when
        WinnersRetriever winnersRetriever = resultsCheckerFacade.winnersRetriever();
        List<Player> players = winnersRetriever.retrieveWinners(allTicketsByDate, winningNumbers);

        Player player1 = players.get(0);
        Player player2 = players.get(1);
        Player player3 = players.get(2);
        //then
        assertThat(players).isNotNull();
        assertFalse(player1.isWinner());
        assertFalse(player2.isWinner());
        assertFalse(player3.isWinner());

        assertThat(player1.hitNumbers().size()).isEqualTo(0);
        assertThat(player2.hitNumbers().size()).isEqualTo(0);
        assertThat(player3.hitNumbers().size()).isEqualTo(0);
    }

    @Test
    public void should_return_empty_players_when_tickets_is_empty() {
        //given
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .resultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningTicketFacade, playerRepository);

        Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5, 6, 7);
        //when
        WinnersRetriever winnersRetriever = resultsCheckerFacade.winnersRetriever();
        List<Player> players = winnersRetriever.retrieveWinners(Collections.emptyList(), winningNumbers);
        //then
        assertTrue(players.isEmpty());
    }

    @Test
    public void should_return_no_winners_when_tickets_is_empty_and_winning_numbers_is_null() {
        //given
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .resultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningTicketFacade, playerRepository);

        //when
        WinnersRetriever winnersRetriever = resultsCheckerFacade.winnersRetriever();

        List<Player> players = winnersRetriever.retrieveWinners(Collections.emptyList(), null);
        //then
        assertThat(players).isNullOrEmpty();
    }
}
