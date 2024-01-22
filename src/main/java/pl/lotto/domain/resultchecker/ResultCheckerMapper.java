package pl.lotto.domain.resultchecker;

import lombok.AllArgsConstructor;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.resultchecker.dto.TicketResponseDto;

import java.util.List;
import java.util.Set;

import static pl.lotto.domain.resultchecker.ResultCheckerMessageProvider.WIN;

@AllArgsConstructor
class ResultCheckerMapper {

    static TicketResponseDto mapTicketResult(Set<Integer> hitNumbers, TicketDto ticket) {
        return TicketResponseDto.builder()
                        .ticketUUID(ticket.ticketUUID())
                        .drawDate(ticket.drawDate())
                        .hitNumbers(hitNumbers)
                        .isWinner(true)
                        .message(WIN)
                .build();
    }

    public static TicketResponseDto mapToResultResponseDto(List<WinningTicket> winningTicketSaved) {
        return winningTicketSaved.stream()
                .map(ticketResultSaved ->
                        TicketResponseDto.builder()
                                .ticketUUID(ticketResultSaved.ticketUUID())
                                .inputNumbers(ticketResultSaved.inputNumbers())
                                .drawDate(ticketResultSaved.drawDate())
                                .hitNumbers(ticketResultSaved.hitNumbers())
                                .isWinner(ticketResultSaved.isWinner())
                                .message(ticketResultSaved.message())
                                .build())
                .findAny()
                .orElseThrow(() -> new RuntimeException("Winning ticket not saved to database"));
    }

    public static TicketResponseDto mapToResultResponse(WinningTicket winningTicket) {
        return TicketResponseDto.builder()
                .ticketUUID(winningTicket.ticketUUID())
                .inputNumbers(winningTicket.inputNumbers())
                .drawDate(winningTicket.drawDate())
                .hitNumbers(winningTicket.hitNumbers())
                .isWinner(winningTicket.isWinner())
                .message(winningTicket.message())
                .build();
    }
}