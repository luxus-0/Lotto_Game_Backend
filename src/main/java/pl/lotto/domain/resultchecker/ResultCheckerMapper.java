package pl.lotto.domain.resultchecker;

import lombok.AllArgsConstructor;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.resultchecker.dto.ResultCheckerResponseDto;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
class ResultCheckerMapper {

    static ResultCheckerResponseDto mapTicketResult(Set<Integer> hitNumbers, TicketDto ticket) {
        return ResultCheckerResponseDto.builder()
                        .ticketUUID(ticket.ticketUUID())
                        .drawDate(ticket.drawDate())
                        .hitNumbers(hitNumbers)
                        .inputNumbers(ticket.inputNumbers())
                .isWinner(ticket.isWinner())
                .message(ticket.message())
                .build();
    }

    public static ResultCheckerResponseDto mapToResultResponseDto(List<TicketResults> ticketSaved) {
        return ticketSaved.stream()
                .map(ticketResultSaved ->
                        ResultCheckerResponseDto.builder()
                                .ticketUUID(ticketResultSaved.ticketUUID())
                                .inputNumbers(ticketResultSaved.inputNumbers())
                                .drawDate(ticketResultSaved.drawDate())
                                .hitNumbers(ticketResultSaved.hitNumbers())
                                .isWinner(ticketResultSaved.isWinner())
                                .message(ticketResultSaved.message())
                                .build())
                .findAny()
                .orElseThrow(() -> new RuntimeException("Ticket not saved to database"));
    }

    public static ResultCheckerResponseDto mapToResultResponse(TicketResults ticketResults) {
        return ResultCheckerResponseDto.builder()
                .ticketUUID(ticketResults.ticketUUID())
                .inputNumbers(ticketResults.inputNumbers())
                .drawDate(ticketResults.drawDate())
                .hitNumbers(ticketResults.hitNumbers())
                .isWinner(ticketResults.isWinner())
                .message(ticketResults.message())
                .build();
    }
}