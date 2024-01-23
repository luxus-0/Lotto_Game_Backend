package pl.lotto.domain.resultchecker;

import lombok.AllArgsConstructor;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.resultchecker.dto.TicketResponseDto;
import pl.lotto.domain.winningnumbers.WinningTicket;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class ResultCheckerMapper {

    public static TicketResponseDto toTicketResponseDto(Set<Integer> hitNumbers, TicketDto ticket) {
        return TicketResponseDto.builder()
                        .ticketUUID(ticket.ticketUUID())
                        .inputNumbers(ticket.inputNumbers())
                        .drawDate(ticket.drawDate())
                        .hitNumbers(hitNumbers)
                        .isWinner(ticket.isWinner())
                        .message(ticket.message())
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