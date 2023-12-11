package pl.lotto.domain.resultchecker;

import lombok.AllArgsConstructor;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.resultchecker.dto.ResultCheckerResponseDto;

import java.util.List;
import java.util.Set;

import static pl.lotto.domain.resultchecker.ResultCheckerMessageProvider.LOSE;
import static pl.lotto.domain.resultchecker.ResultCheckerMessageProvider.WIN;

@AllArgsConstructor
class ResultCheckerMapper {

    static ResultCheckerResponse mapPlayerLose(Set<Integer> hitNumbers, TicketDto ticket) {
        return ResultCheckerResponse.builder()
                        .ticketUUID(ticket.ticketUUID())
                        .inputNumbers(ticket.inputNumbers())
                        .drawDate(ticket.drawDate())
                        .isWinner(false)
                        .hitNumbers(hitNumbers)
                        .message(LOSE)
                        .build();
    }

    static ResultCheckerResponse mapPlayerWin(Set<Integer> hitNumbers, TicketDto ticket) {
        return ResultCheckerResponse.builder()
                        .ticketUUID(ticket.ticketUUID())
                        .inputNumbers(ticket.inputNumbers())
                        .drawDate(ticket.drawDate())
                        .isWinner(true)
                        .hitNumbers(hitNumbers)
                        .message(WIN)
                        .build();
    }

   static ResultCheckerResponseDto mapToTicketWinningNumbers(ResultCheckerResponse result) {
        return ResultCheckerResponseDto.builder()
                .ticketsWinningNumbersSaved(List.of(result))
                .build();
    }

}