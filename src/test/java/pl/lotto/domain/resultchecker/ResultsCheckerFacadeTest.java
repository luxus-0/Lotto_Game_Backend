package pl.lotto.domain.resultchecker;

import org.junit.jupiter.api.Test;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.resultchecker.dto.TicketResponseDto;
import pl.lotto.domain.winningnumbers.WinningNumbersFacade;
import pl.lotto.domain.winningnumbers.WinningTicket;
import pl.lotto.domain.winningnumbers.dto.WinningTicketResponseDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.lotto.domain.resultchecker.ResultCheckerMessageProvider.LOSE;
import static pl.lotto.domain.resultchecker.ResultCheckerMessageProvider.WIN;

class ResultsCheckerFacadeTest {

    WinningNumbersFacade winningNumbersFacade = mock(WinningNumbersFacade.class);
    NumberReceiverFacade numberReceiverFacade = mock(NumberReceiverFacade.class);
    ResultCheckerRepository resultCheckerRepository = mock(ResultCheckerRepository.class);


    @Test
    public void should_generate_one_player_win() throws Exception {
        //given
        ResultsCheckerFacade resultCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .resultsCheckerFacade(numberReceiverFacade, winningNumbersFacade, resultCheckerRepository);

        LocalDateTime drawDate = LocalDateTime.of(2023, 11, 25, 12, 0, 0);
        String ticketUUID1 = "550e8125-e29b-41d4-a716-446655440000";
        String ticketUUID2 = "550e8547-e29b-41d4-a716-446655440000";

        WinningTicket winningTicket1 = WinningTicket.builder()
                .ticketUUID(ticketUUID1)
                .inputNumbers(Set.of(30, 40, 50, 60, 70, 80))
                .hitNumbers(Set.of())
                .drawDate(drawDate)
                .isWinner(false)
                .message(LOSE)
                .build();

        WinningTicket winningTicket2 = WinningTicket.builder()
                .ticketUUID(ticketUUID2)
                .inputNumbers(Set.of(17, 22, 33, 44, 55, 66))
                .hitNumbers(Set.of(17, 22, 33))
                .drawDate(drawDate)
                .isWinner(true)
                .message(WIN)
                .build();

        when(winningNumbersFacade.generateWinningNumbers()).thenReturn(
                WinningTicketResponseDto.builder()
                        .ticketUUID(ticketUUID1)
                        .winningNumbers(Set.of(3, 4, 5))
                        .drawDate(drawDate)
                        .isWinner(true)
                        .message(WIN)
                        .build());

        when(resultCheckerRepository.findAllByTicketUUID(ticketUUID1)).thenReturn(Optional.of(winningTicket1));
        when(resultCheckerRepository.findAllByTicketUUID(ticketUUID2)).thenReturn(Optional.of(winningTicket2));
        when(resultCheckerRepository.saveAll(anyList())).thenReturn(List.of(winningTicket2));
        //when
        TicketResponseDto playerResult = resultCheckerFacade.generateResults();
        //then
        assertTrue(playerResult.isWinner());
        assertThat(playerResult).isNotNull();
    }

    @Test
    public void should_return_message_lose_when_winning_numbers_is_empty() throws Exception {
        //given
        ResultsCheckerFacade resultCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .resultsCheckerFacade(numberReceiverFacade, winningNumbersFacade, resultCheckerRepository);

        when(winningNumbersFacade.generateWinningNumbers()).thenReturn(
                WinningTicketResponseDto.builder()
                        .winningNumbers(Set.of())
                        .build());

        TicketResponseDto results = resultCheckerFacade.generateResults();

        //when and then
        assertThat(results.message()).isEqualTo(LOSE);
    }

    @Test
    public void should_generate_lose_message_when_no_winning_numbers() {
        //given
        when(winningNumbersFacade.generateWinningNumbers()).thenReturn(
                WinningTicketResponseDto.builder()
                        .ticketUUID("564e8400-e29b-41d4-a716-446655440000")
                        .drawDate(LocalDateTime.of(2021, 12, 11, 12, 0, 0, 0))
                        .winningNumbers(Collections.emptySet())
                        .message(LOSE)
                        .build()
        );
        //when
        WinningTicketResponseDto ticket = winningNumbersFacade.generateWinningNumbers();
        //then
        assertThat(ticket.message()).isEqualTo(LOSE);
    }

    @Test
    public void should_return_result_with_correct_credentials() throws Exception {
        //given
        ResultsCheckerFacade resultCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .resultsCheckerFacade(numberReceiverFacade, winningNumbersFacade, resultCheckerRepository);

        LocalDateTime drawDate = LocalDateTime.of(2023, 12, 25, 12, 0, 0);
        String ticketUUID1 = "550e8400-e29b-41d4-a716-446655440000";
        String ticketUUID2 = "648e8400-e29b-41d4-a716-446655440000";

        WinningTicket winningTicket1 = WinningTicket.builder()
                .ticketUUID(ticketUUID1)
                .inputNumbers(Set.of(4, 5, 6, 10, 11, 12))
                .hitNumbers(Set.of(4, 5, 6, 10))
                .drawDate(drawDate)
                .build();

        WinningTicket winningTicket2 = WinningTicket.builder()
                .ticketUUID(ticketUUID2)
                .inputNumbers(Set.of(9, 13, 15, 17, 14, 20))
                .hitNumbers(Set.of(9, 17, 13))
                .drawDate(drawDate)
                .build();


        when(resultCheckerRepository.findAllByTicketUUID(ticketUUID1))
               .thenReturn(Optional.of(winningTicket1));

        when(resultCheckerRepository.findAllByTicketUUID(ticketUUID2))
                .thenReturn(Optional.of(winningTicket2));
        //when

        TicketResponseDto actualResult = resultCheckerFacade.findResultByTicketUUID(ticketUUID1);
        //then
        TicketResponseDto expectedResult = TicketResponseDto.builder()
                .ticketUUID(ticketUUID1)
                .inputNumbers(Set.of(4, 5, 6, 10, 11, 12))
                .hitNumbers(Set.of(4, 5, 6, 10))
                .drawDate(drawDate)
                .build();


        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void should_return_win_player_when_hit_numbers_are_three() {
        //given
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .resultsCheckerFacade(numberReceiverFacade, winningNumbersFacade, resultCheckerRepository);

        TicketDto ticket1 = TicketDto.builder()
                .ticketUUID("1234")
                .inputNumbers(Set.of(1, 2, 3, 4, 11, 17, 19))
                .hitNumbers(Set.of(4, 11))
                .drawDate(LocalDateTime.of(2023, 12, 25, 12, 0, 0))
                .build();

        TicketDto ticket2 = TicketDto.builder()
                .ticketUUID("5678")
                .inputNumbers(Set.of(1, 2, 3, 4, 28, 39, 42))
                .hitNumbers(Set.of(1))
                .drawDate(LocalDateTime.of(2023, 12, 25, 12, 0, 0))
                .isWinner(false)
                .build();

        TicketDto ticket3 = TicketDto.builder()
                .ticketUUID("9844")
                .inputNumbers(Set.of(1, 2, 3, 4, 20, 35, 45))
                .hitNumbers(Set.of(20, 35, 45))
                .drawDate(LocalDateTime.of(2023, 12, 25, 12, 0, 0))
                .isWinner(true)
                .build();

        List<TicketDto> tickets = List.of(ticket1, ticket2, ticket3);
        Set<Integer> winningNumbers = Set.of(20, 35, 45);

        //when
        List<TicketResponseDto> results = resultsCheckerFacade.generateWinTicket(winningNumbers, tickets);

        TicketResponseDto player1 = results.get(0);
        TicketResponseDto player2 = results.get(1);
        TicketResponseDto player3 = results.get(2);

        //then
        assertEquals(3, results.size());
        assertFalse(player1.isWinner());
        assertFalse(player2.isWinner());
        assertTrue(player3.isWinner());
    }

    @Test
    public void should_return_not_win_players_when_hit_numbers_are_less_than_three() {
        //given
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .resultsCheckerFacade(numberReceiverFacade, winningNumbersFacade, resultCheckerRepository);

        TicketDto ticket1 = TicketDto.builder()
                .ticketUUID("1234")
                .inputNumbers(Set.of(1, 2, 9, 10, 11, 17, 19))
                .hitNumbers(Set.of(1,2))
                .drawDate(LocalDateTime.of(2022, 4, 8, 12, 0, 0))
                .build();

        TicketDto ticket2 = TicketDto.builder()
                .ticketUUID("5678")
                .inputNumbers(Set.of(1, 3, 22, 8, 20, 35, 45))
                .hitNumbers(Set.of(8,20))
                .drawDate(LocalDateTime.of(2022, 4, 8, 12, 0, 0))
                .build();

        TicketDto ticket3 = TicketDto.builder()
                .ticketUUID("1111")
                .inputNumbers(Set.of(1, 6, 14, 20, 90, 30, 8))
                .hitNumbers(Set.of(6))
                .drawDate(LocalDateTime.of(2022, 4, 8, 12, 0, 0))
                .build();

        List<TicketDto> tickets = List.of(ticket1, ticket2, ticket3);
        Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5, 6, 7);

        //when
        List<TicketResponseDto> results = resultsCheckerFacade.generateWinTicket(winningNumbers, tickets);

        TicketResponseDto result1 = results.get(0);
        TicketResponseDto result2 = results.get(1);
        TicketResponseDto result3 = results.get(2);
        //then
        assertThat(results).isNotNull();
        assertFalse(result1.isWinner());
        assertFalse(result2.isWinner());
        assertFalse(result3.isWinner());
    }

    @Test
    public void should_return_not_win_player_when_hit_numbers_are_empty() {
        //given
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .resultsCheckerFacade(numberReceiverFacade, winningNumbersFacade, resultCheckerRepository);

        TicketDto ticket1 = TicketDto.builder()
                .ticketUUID("1234")
                .inputNumbers(Set.of(22, 45, 9, 10, 11, 17, 19))
                .hitNumbers(Collections.emptySet())
                .drawDate(LocalDateTime.of(2022, 4, 8, 12, 0, 0))
                .build();

        TicketDto ticket2 = TicketDto.builder()
                .ticketUUID("5678")
                .inputNumbers(Set.of(40, 17, 22, 12, 20, 35, 45))
                .hitNumbers(Collections.emptySet())
                .drawDate(LocalDateTime.of(2022, 4, 8, 12, 0, 0))
                .build();

        TicketDto ticket3 = TicketDto.builder()
                .ticketUUID("1111")
                .inputNumbers(Set.of(60, 45, 14, 20, 90, 30, 8))
                .hitNumbers(Collections.emptySet())
                .drawDate(LocalDateTime.of(2022, 4, 8, 12, 0, 0))
                .build();

        List<TicketDto> tickets = List.of(ticket1, ticket2, ticket3);
        Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5, 6, 7);

        //when
        List<TicketResponseDto> results = resultsCheckerFacade.generateWinTicket(winningNumbers, tickets);

        TicketResponseDto result1 = results.get(0);
        TicketResponseDto result2 = results.get(1);
        TicketResponseDto result3 = results.get(2);

        //then
        assertThat(results).isNotNull();
        assertFalse(result1.isWinner());
        assertFalse(result2.isWinner());
        assertFalse(result3.isWinner());
    }

    @Test
    public void should_return_not_win_players_when_input_numbers_are_empty() {
        //given
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .resultsCheckerFacade(numberReceiverFacade, winningNumbersFacade, resultCheckerRepository);

        TicketDto ticket1 = TicketDto.builder()
                .ticketUUID("1234")
                .inputNumbers(Set.of())
                .hitNumbers(Set.of())
                .drawDate(LocalDateTime.of(2022, 4, 8, 12, 0, 0))
                .build();

        TicketDto ticket2 = TicketDto.builder()
                .ticketUUID("5678")
                .inputNumbers(Set.of())
                .hitNumbers(Set.of())
                .drawDate(LocalDateTime.of(2022, 4, 8, 12, 0, 0))
                .build();

        TicketDto ticket3 = TicketDto.builder()
                .ticketUUID("1111")
                .inputNumbers(Set.of())
                .hitNumbers(Set.of())
                .drawDate(LocalDateTime.of(2022, 4, 8, 12, 0, 0))
                .build();

        List<TicketDto> tickets = List.of(ticket1, ticket2, ticket3);
        Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5, 6, 7);

        //when
        List<TicketResponseDto> results = resultsCheckerFacade.generateWinTicket(winningNumbers, tickets);

        TicketResponseDto result1 = results.get(0);
        TicketResponseDto result2 = results.get(1);
        TicketResponseDto result3 = results.get(2);
        //then
        assertThat(results).isNotNull();
        assertFalse(result1.isWinner());
        assertFalse(result2.isWinner());
        assertFalse(result3.isWinner());
    }
    @Test
    public void should_return_empty_players_when_tickets_is_empty() {
        //given
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .resultsCheckerFacade(numberReceiverFacade, winningNumbersFacade, resultCheckerRepository);

        Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5, 6, 7);
        //when
        List<TicketResponseDto> results = resultsCheckerFacade.generateWinTicket(winningNumbers, Collections.emptyList());

        //then
        assertTrue(results.isEmpty());
    }

    @Test
    public void should_return_no_winners_when_tickets_is_empty_and_winning_numbers_is_null() {
        //given
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .resultsCheckerFacade(numberReceiverFacade, winningNumbersFacade, resultCheckerRepository);

        //when
        List<TicketResponseDto> results = resultsCheckerFacade.generateWinTicket(null, Collections.emptyList());

        //then
        assertThat(results).isNullOrEmpty();
    }
}
