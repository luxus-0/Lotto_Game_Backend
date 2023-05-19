package pl.lotto.domain.resultchecker;

import org.junit.jupiter.api.Test;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.numbersgenerator.WinningNumbersFacade;
import pl.lotto.domain.numbersgenerator.WinningNumbersNotFoundException;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;
import pl.lotto.domain.resultannouncer.ResultLotto;
import pl.lotto.domain.resultchecker.dto.PlayersDto;
import pl.lotto.domain.resultchecker.dto.ResultDto;
import pl.lotto.domain.resultchecker.exceptions.PlayerResultNotFoundException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResultsCheckerFacadeTest {

    private final WinningNumbersFacade winningNumbersFacade = mock(WinningNumbersFacade.class);
    private final NumberReceiverFacade numberReceiverFacade = mock(NumberReceiverFacade.class);

    private final DrawDateFacade drawDateFacade = mock(DrawDateFacade.class);

    @Test
    public void should_generate_all_players_with_correct_message() {
        //given
        PlayerRepository playerRepository = new PlayerRepositoryTestImpl();
        ResultsCheckerFacade resultCheckerFacade = new ResultsCheckerFacadeConfiguration().resultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningNumbersFacade, playerRepository);
        LocalDateTime drawDate = LocalDateTime.of(2023, 3, 8, 12, 0, 0, 0);

        when(winningNumbersFacade.generateWinningNumbers()).thenReturn(
                WinningNumbersDto.builder()
                        .winningNumbers(Set.of(1, 3, 5, 7, 9, 11))
                        .drawDate(drawDate)
                        .build());
        when(numberReceiverFacade.retrieveAllTicketByDrawDate(drawDate)).thenReturn(
                List.of(
                        TicketDto.builder()
                                .ticketId("001")
                                .numbers(Set.of(1, 3, 5, 7, 9, 11))
                                .drawDate(drawDate)
                                .build(),
                        TicketDto.builder()
                                .ticketId("002")
                                .numbers(Set.of(1, 3, 5, 7, 9, 11))
                                .drawDate(drawDate)
                                .build(),
                        TicketDto.builder()
                                .ticketId("003")
                                .numbers(Set.of(1, 3, 5, 7, 9, 11))
                                .drawDate(drawDate)
                                .build()
                ));
        //when
        PlayersDto playersDto = resultCheckerFacade.generateResults();
        //then
        String messageExpected = playersDto.results().stream().map(ResultLotto::message).findAny().orElseThrow(() -> new WinningNumbersNotFoundException("Winning numbers not found"));

        assertThat(playersDto).isNotNull();
        assertThat(messageExpected).isEqualTo("Winners found");
    }

    @Test
    public void should_generate_fail_message_when_winning_numbers_is_empty() {
        //given
        PlayerRepository playerRepository = new PlayerRepositoryTestImpl();
        ResultsCheckerFacade resultCheckerFacade = new ResultsCheckerFacadeConfiguration().resultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningNumbersFacade, playerRepository);

        when(winningNumbersFacade.generateWinningNumbers()).thenReturn(
                WinningNumbersDto.builder()
                        .winningNumbers(Set.of())
                        .build()
        );
        //when
        PlayersDto playersDto = resultCheckerFacade.generateResults();
        String message = playersDto.results().stream().map(ResultLotto::message).findAny().orElseThrow(() -> new PlayerResultNotFoundException("Player result not found"));
        //then
        assertThat(message).isEqualTo("Winners not found");
    }

    @Test
    public void should_generate_fail_message_when_winning_numbers_equal_null() {
        //given
        PlayerRepository playerRepository = new PlayerRepositoryTestImpl();
        ResultsCheckerFacade resultCheckerFacade = new ResultsCheckerFacadeConfiguration().resultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningNumbersFacade, playerRepository);

        when(winningNumbersFacade.generateWinningNumbers()).thenReturn(
                WinningNumbersDto.builder()
                        .winningNumbers(null)
                        .build()
        );
        //when
        PlayersDto playersDto = resultCheckerFacade.generateResults();
        String message = playersDto.results().stream().map(ResultLotto::message).findAny().orElse("");
        //then
        assertThat(message).isEqualTo("Winners not found");
    }

    @Test
    public void should_generate_correct_message_when_winning_numbers_appear() {
        //given
        PlayerRepository playerRepository = new PlayerRepositoryTestImpl();
        ResultsCheckerFacade resultCheckerFacade = new ResultsCheckerFacadeConfiguration().resultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningNumbersFacade, playerRepository);

        when(winningNumbersFacade.generateWinningNumbers()).thenReturn(
                WinningNumbersDto.builder()
                        .winningNumbers(Set.of(99, 80, 70, 60, 50, 11))
                        .build()
        );
        //when
        PlayersDto playersDto = resultCheckerFacade.generateResults();
        String message = playersDto.results().stream().map(ResultLotto::message).findAny().orElseThrow();
        //then
        assertThat(message).isEqualTo("Winners found");
    }

    @Test
    public void should_return_result_with_correct_credentials() {
        //given
        PlayerRepository playerRepository = new PlayerRepositoryTestImpl();
        ResultsCheckerFacade resultCheckerFacade = new ResultsCheckerFacadeConfiguration().resultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningNumbersFacade, playerRepository);

        LocalDateTime drawDate = LocalDateTime.of(2022, 12, 17, 12, 0, 0);
        String ticketId = "001";

        when(winningNumbersFacade.generateWinningNumbers()).thenReturn(WinningNumbersDto.builder()
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .build());
        when(numberReceiverFacade.retrieveAllTicketByDrawDate(drawDate)).thenReturn(
                List.of(TicketDto.builder()
                                .ticketId(ticketId)
                                .numbers(Set.of(7, 8, 9, 10, 11, 12))
                                .drawDate(drawDate)
                                .build(),
                        TicketDto.builder()
                                .ticketId("002")
                                .numbers(Set.of(7, 8, 9, 10, 11, 13))
                                .drawDate(drawDate)
                                .build(),
                        TicketDto.builder()
                                .ticketId("003")
                                .numbers(Set.of(7, 8, 9, 10, 11, 14))
                                .drawDate(drawDate)
                                .build())
        );
        resultCheckerFacade.generateResults();
        playerRepository.save(Player.builder()
                        .ticketId(ticketId)
                        .numbers(Set.of(7, 8, 9, 10, 11, 12))
                        .hitNumbers(Set.of(7, 8, 9, 10, 11, 12))
                        .drawDate(drawDate)
                .build());
        //when

        ResultDto resultDto = resultCheckerFacade.findResultByTicketId(ticketId);
        //then
        ResultDto expectedResult = ResultDto.builder()
                .ticketId(ticketId)
                .numbers(Set.of(7, 8, 9, 10, 11, 12))
                .hitNumbers(Set.of(7, 8, 9, 10, 11, 12))
                .drawDate(drawDate)
                .isWinner(resultDto.isWinner())
                .build();
        assertThat(resultDto).isEqualTo(expectedResult);
    }

    @Test
    public void should_return_win_player_when_hit_numbers_are_more_than_three() {
        //given
        PlayerRepository playerRepository = new PlayerRepositoryTestImpl();
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration().resultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningNumbersFacade, playerRepository);

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
                .drawDate(LocalDateTime.of(2022, 4, 8, 12, 0, 0)
                )
                .build();

        List<Ticket> allTicketsByDate = List.of(ticket1, ticket2, ticket3);
        Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5, 6, 7);

        //when
        WinnersRetriever winnersRetriever = resultsCheckerFacade.winnersRetriever;
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
        PlayerRepository playerRepository = new PlayerRepositoryTestImpl();
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration().resultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningNumbersFacade, playerRepository);

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
        WinnersRetriever winnersRetriever = resultsCheckerFacade.winnersRetriever;
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
        PlayerRepository playerRepository = new PlayerRepositoryTestImpl();
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration().resultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningNumbersFacade, playerRepository);

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
        WinnersRetriever winnersRetriever = resultsCheckerFacade.winnersRetriever;
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
        PlayerRepository playerRepository = new PlayerRepositoryTestImpl();
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration().resultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningNumbersFacade, playerRepository);

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
        WinnersRetriever winnersRetriever = resultsCheckerFacade.winnersRetriever;
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
    public void should_return_null_player_when_tickets_collection_is_empty() {
        //given
        PlayerRepository playerRepository = new PlayerRepositoryTestImpl();
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration().resultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningNumbersFacade, playerRepository);

        Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5, 6, 7);
        //when
        WinnersRetriever winnersRetriever = resultsCheckerFacade.winnersRetriever;
        List<Player> players = winnersRetriever.retrieveWinners(Collections.emptyList(), winningNumbers);
        //then
        assertTrue(players.isEmpty());
    }

    @Test
    public void should_return_null_player_when_tickets_is_empty_and_winning_numbers_is_null() {
        //given
        PlayerRepository playerRepository = new PlayerRepositoryTestImpl();
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration().resultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningNumbersFacade, playerRepository);

        //when
        WinnersRetriever winnersRetriever = resultsCheckerFacade.winnersRetriever;

        List<Player> players = winnersRetriever.retrieveWinners(Collections.emptyList(), null);
        //then
        assertThat(players).isNullOrEmpty();
    }
}
