package integration.cache.redis;

import integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import pl.lotto.domain.winningnumbers.WinningNumbersFacade;
import pl.lotto.domain.winningnumbers.dto.WinningTicketResponseDto;
import pl.lotto.domain.resultchecker.ResultsCheckerFacade;
import pl.lotto.domain.resultchecker.WinningTicket;
import pl.lotto.domain.resultchecker.dto.TicketResponseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static integration.cache.redis.constants.RedisResultCheckerMapper.toResultChecker;
import static integration.cache.redis.constants.RedisWinningTicketResponse.createWinningTicketResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static pl.lotto.domain.resultchecker.ResultCheckerMessageProvider.WIN;

public class RedisResultCheckerIntegrationTest extends BaseIntegrationTest {

    @MockBean
    WinningNumbersFacade winningNumbersFacade;
    @MockBean
    ResultsCheckerFacade resultsCheckerFacade;
    @Autowired
    CacheManager cacheManager;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Test
    public void should_return_two_results_to_cache() {
        // Given
        WinningTicketResponseDto winningTicketResponse = createWinningTicketResponse();
        when(winningNumbersFacade.generateWinningNumbers()).thenReturn(winningTicketResponse);

        TicketResponseDto firstCallResult = resultsCheckerFacade.generateResults();

        assertEquals(firstCallResult, Objects.requireNonNull(cacheManager.getCache("results")).get("result", TicketResponseDto.class));

        TicketResponseDto secondCallResult = resultsCheckerFacade.generateResults();

        assertEquals(firstCallResult, secondCallResult);
    }

    @Test
    public void should_return_saved_result_checker_to_cache() {

        List<WinningTicket> results = resultCheckerRepository.saveAll(List.of(WinningTicket.builder()
                .ticketUUID("12345")
                .inputNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3))
                .drawDate(LocalDateTime.now())
                .message(WIN)
                .isWinner(true)
                .build()));

        TicketResponseDto result = toResultChecker(results);

        redisTemplate.opsForValue().set(result.ticketUUID(), result.isWinner());

        assertNotNull(result);
        assertThat(redisTemplate.opsForValue().get(result.ticketUUID())).isEqualTo(true);
    }
}
