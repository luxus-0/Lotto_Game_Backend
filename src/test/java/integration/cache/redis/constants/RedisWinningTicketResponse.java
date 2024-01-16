package integration.cache.redis.constants;

import pl.lotto.domain.numbersgenerator.dto.WinningTicketResponseDto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public class RedisWinningTicketResponse {
    public static WinningTicketResponseDto createWinningTicketResponse() {
        {
            Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5,6);
            LocalDateTime drawDate = LocalDateTime.now();
            String ticketUUID = UUID.randomUUID().toString();

            return WinningTicketResponseDto.builder()
                    .winningNumbers(winningNumbers)
                    .drawDate(drawDate)
                    .ticketUUID(ticketUUID)
                    .build();
        }
    }
}
