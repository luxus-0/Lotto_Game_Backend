package pl.lotto.domain.resultchecker;

import lombok.AllArgsConstructor;
import pl.lotto.domain.resultchecker.dto.ResultResponseDto;
import pl.lotto.domain.numberreceiver.dto.TicketDto;

import java.util.List;
import java.util.Set;

import static pl.lotto.domain.resultchecker.ResultCheckerMessageProvider.LOSE;
import static pl.lotto.domain.resultchecker.ResultCheckerMessageProvider.WIN;

@AllArgsConstructor
class ResultCheckerMapper {

    static ResultResponseDto mapTicketResult(Set<Integer> hitNumbers, TicketDto ticket) {
        return ResultResponseDto.builder()
                        .ticketUUID(ticket.ticketUUID())
                        .drawDate(ticket.drawDate())
                        .hitNumbers(hitNumbers)
                        .inputNumbers(ticket.inputNumbers())
                        .isWinner(true)
                        .message(WIN)
                .build();
    }

    public static ResultResponseDto mapToResultResponseDto(List<TicketResults> ticketSaved) {
        return ticketSaved.stream().map(ticketResultSaved ->
                ResultResponseDto.builder()
                        .ticketUUID(ticketResultSaved.ticketUUID())
                        .inputNumbers(ticketResultSaved.inputNumbers())
                        .drawDate(ticketResultSaved.drawDate())
                        .hitNumbers(ticketResultSaved.hitNumbers())
                        .isWinner(ticketResultSaved.isWinner())
                        .message(ticketResultSaved.message())
                        .build())
                .findAny()
                .orElse(ResultResponseDto.builder()
                        .build());
    }

    public static ResultResponseDto mapToResultResponse(TicketResults ticketResults) {
        return ResultResponseDto.builder()
                .ticketUUID(ticketResults.ticketUUID())
                .inputNumbers(ticketResults.inputNumbers())
                .drawDate(ticketResults.drawDate())
                .hitNumbers(ticketResults.hitNumbers())
                .isWinner(ticketResults.isWinner())
                .message(ticketResults.message())
                .build();
    }
}