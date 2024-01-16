package integration.cache.redis.constants;

import pl.lotto.domain.resultchecker.TicketResults;
import pl.lotto.domain.resultchecker.dto.ResultCheckerResponseDto;

import java.util.List;

public class RedisResultCheckerMapper {
    public static ResultCheckerResponseDto toResultChecker(List<TicketResults> ticketSaved) {
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
}
