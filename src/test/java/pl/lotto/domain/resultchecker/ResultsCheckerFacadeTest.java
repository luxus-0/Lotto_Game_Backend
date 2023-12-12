package pl.lotto.domain.resultchecker;

import org.junit.jupiter.api.Test;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.numberreceiver.exceptions.WinningTicketNotFoundException;
import pl.lotto.domain.numbersgenerator.WinningTicketFacade;
import pl.lotto.domain.numbersgenerator.dto.WinningTicketResponseDto;
import pl.lotto.domain.numbersgenerator.exceptions.WinningNumbersNotFoundException;
import pl.lotto.domain.resultchecker.dto.ResultResponseDto;

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
import static pl.lotto.domain.resultchecker.ResultCheckerMessageProvider.LOSE;

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

        TicketResults ticket1 = TicketResults.builder()
                .ticketUUID(ticketUUID1)
                .inputNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(3, 4))
                .drawDate(drawDate)
                .build();

        TicketResults ticket2 = TicketResults.builder()
                .ticketUUID(ticketUUID2)
                .inputNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(3, 4, 5))
                .drawDate(drawDate)
                .build();

        when(winningTicketFacade.generateWinningTicket()).thenReturn(
                WinningTicketResponseDto.builder()
                        .ticketUUID(ticketUUID1)
                        .winningNumbers(Set.of(3, 4, 5))
                        .drawDate(drawDate)
                        .isWinner(true)
                        .build());

        when(resultCheckerRepository.findAllByTicketUUID(ticketUUID1)).thenReturn(List.of(ticket1));
        when(resultCheckerRepository.findAllByTicketUUID(ticketUUID2)).thenReturn(List.of(ticket2));
        when(resultCheckerRepository.saveAll(anyList())).thenReturn(List.of(ticket2));
        //when
        ResultResponseDto playerResult = resultCheckerFacade.generateResults();
        //then
        assertTrue(playerResult.isWinner());
        assertThat(playerResult).isNotNull();
    }

    @Test
    public void should_throwing_winning_numbers_not_found_when_winning_numbers_is_empty() throws WinningTicketNotFoundException {
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
                .isInstanceOf(WinningNumbersNotFoundException.class);
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
        String ticketUUID2 = "648e8400-e29b-41d4-a716-446655440000";

        TicketResults ticket1 = TicketResults.builder()
                .ticketUUID(ticketUUID1)
                .inputNumbers(Set.of(4, 5, 6, 10, 11, 12))
                .hitNumbers(Set.of(4, 5, 6, 10))
                .drawDate(drawDate)
                .build();

        TicketResults ticket2 = TicketResults.builder()
                .ticketUUID(ticketUUID2)
                .inputNumbers(Set.of(9, 13, 15, 17, 14, 20))
                .hitNumbers(Set.of(9, 17, 13))
                .drawDate(drawDate)
                .build();


        when(resultCheckerRepository.findAllByTicketUUID(ticketUUID1))
               .thenReturn(List.of(ticket1));

        when(resultCheckerRepository.findAllByTicketUUID(ticketUUID2))
                .thenReturn(List.of(ticket2));
        //when

        ResultResponseDto actualResult = resultCheckerFacade.findResultByTicketUUID(ticketUUID1);
        //then
        ResultResponseDto expectedResult = ResultResponseDto.builder()
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
                .resultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningTicketFacade, resultCheckerRepository);

        TicketDto ticket1 = TicketDto.builder()
                .ticketUUID("1234")
                .inputNumbers(Set.of(1, 2, 3, 4, 11, 17, 19))
                .hitNumbers(Set.of(4, 11))
                .drawDate(LocalDateTime.of(2023, 12, 25, 12, 0, 0))
                .build();

        TicketDto ticket2 = TicketDto.builder()
                .ticketUUID("5678")
                .inputNumbers(Set.of(1, 2, 3, 4, 20, 35, 45))
                .hitNumbers(Set.of(1))
                .drawDate(LocalDateTime.of(2023, 12, 25, 12, 0, 0))
                .build();

        TicketDto ticket3 = TicketDto.builder()
                .ticketUUID("9844")
                .inputNumbers(Set.of(1, 2, 3, 4, 20, 35, 45))
                .hitNumbers(Set.of(20, 35, 45))
                .drawDate(LocalDateTime.of(2023, 12, 25, 12, 0, 0))
                .isWinner(true)
                .build();

        List<TicketDto> tickets = List.of(ticket1, ticket2, ticket3);
        Set<Integer> winningNumbers = Set.of(20, 35, 45, 2);

        //when
        WinnersRetriever winnersRetriever = resultsCheckerFacade.winnersRetriever();
        List<ResultResponseDto> results = winnersRetriever.retrieveWinners(tickets, winningNumbers);

        ResultResponseDto resultCheckerResponse1 = results.get(0);
        ResultResponseDto resultCheckerResponse2 = results.get(1);
        ResultResponseDto resultCheckerResponse3 = results.get(2);

        //then
        assertEquals(3, results.size());
        assertFalse(resultCheckerResponse1.isWinner());
        assertFalse(resultCheckerResponse2.isWinner());
        assertTrue(resultCheckerResponse3.isWinner());
    }

    @Test
    public void should_return_not_win_players_when_hit_numbers_are_less_than_three() {
        //given
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .resultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningTicketFacade, resultCheckerRepository);

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

        List<TicketDto> allTicketsByDate = List.of(ticket1, ticket2, ticket3);
        Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5, 6, 7);

        //when
        WinnersRetriever winnersRetriever = resultsCheckerFacade.winnersRetriever();
        List<ResultResponseDto> results = winnersRetriever.retrieveWinners(allTicketsByDate, winningNumbers);

        ResultResponseDto result1 = results.get(0);
        ResultResponseDto result2 = results.get(1);
        ResultResponseDto result3 = results.get(2);
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
                .resultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningTicketFacade, resultCheckerRepository);

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

        List<TicketDto> allTicketsByDate = List.of(ticket1, ticket2, ticket3);
        Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5, 6, 7);

        //when
        WinnersRetriever winnersRetriever = resultsCheckerFacade.winnersRetriever();
        List<ResultResponseDto> results = winnersRetriever.retrieveWinners(allTicketsByDate, winningNumbers);

        ResultResponseDto result1 = results.get(0);
        ResultResponseDto resul2 = results.get(1);
        ResultResponseDto result3 = results.get(2);
        //then
        assertThat(results).isNotNull();
        assertFalse(result1.isWinner());
        assertFalse(resul2.isWinner());
        assertFalse(result3.isWinner());
    }

    @Test
    public void should_return_not_win_players_when_input_numbers_are_empty() {
        //given
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .resultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningTicketFacade, resultCheckerRepository);

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

        List<TicketDto> allTicketsByDate = List.of(ticket1, ticket2, ticket3);
        Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5, 6, 7);

        //when
        WinnersRetriever winnersRetriever = resultsCheckerFacade.winnersRetriever();
        List<ResultResponseDto> results = winnersRetriever.retrieveWinners(allTicketsByDate, winningNumbers);

        ResultResponseDto result1 = results.get(0);
        ResultResponseDto result2 = results.get(1);
        ResultResponseDto result3 = results.get(2);
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
                .resultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningTicketFacade, resultCheckerRepository);

        Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5, 6, 7);
        //when
        WinnersRetriever winnersRetriever = resultsCheckerFacade.winnersRetriever();
        List<ResultResponseDto> results = winnersRetriever.retrieveWinners(Collections.emptyList(), winningNumbers);
        //then
        assertTrue(results.isEmpty());
    }

    @Test
    public void should_return_no_winners_when_tickets_is_empty_and_winning_numbers_is_null() {
        //given
        ResultsCheckerFacade resultsCheckerFacade = new ResultsCheckerFacadeConfiguration()
                .resultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningTicketFacade, resultCheckerRepository);

        //when
        WinnersRetriever winnersRetriever = resultsCheckerFacade.winnersRetriever();

        List<ResultResponseDto> resultCheckerResponses = winnersRetriever.retrieveWinners(Collections.emptyList(), null);
        //then
        assertThat(resultCheckerResponses).isNullOrEmpty();
    }
}
