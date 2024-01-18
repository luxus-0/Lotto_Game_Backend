package integration.cache.redis.constants;

import pl.lotto.domain.resultchecker.WinningTicket;
import pl.lotto.domain.resultchecker.dto.TicketResponseDto;

import java.util.List;

public class RedisResultCheckerMapper {
    public static TicketResponseDto toResultChecker(List<WinningTicket> winningTicketSaved) {
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
                .orElseThrow(() -> new RuntimeException("WinningTicket not saved to database"));
    }
}
