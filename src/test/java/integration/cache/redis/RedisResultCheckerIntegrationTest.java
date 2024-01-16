package integration.cache.redis;

import integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import pl.lotto.domain.numbersgenerator.WinningNumbersFacade;
import pl.lotto.domain.numbersgenerator.dto.WinningTicketResponseDto;
import pl.lotto.domain.resultchecker.ResultsCheckerFacade;
import pl.lotto.domain.resultchecker.dto.ResultResponseDto;

import java.util.Objects;

import static integration.cache.redis.constants.WinningTicketResponse.createWinningTicketResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class RedisResultCheckerIntegrationTest extends BaseIntegrationTest {

    @MockBean
    WinningNumbersFacade winningNumbersFacade;
    @MockBean
    ResultsCheckerFacade resultsCheckerFacade;
    @Autowired
    public CacheManager cacheManager;
    @Test
    public void should_return_two_cache_results_when_generate_winning_ticket() {
        // Given
        WinningTicketResponseDto winningTicketResponse = createWinningTicketResponse();
        when(winningNumbersFacade.generateWinningNumbers()).thenReturn(winningTicketResponse);

        ResultResponseDto firstCallResult = resultsCheckerFacade.generateResults();

        assertEquals(firstCallResult, Objects.requireNonNull(cacheManager.getCache("results")).get("result", ResultResponseDto.class));

        ResultResponseDto secondCallResult = resultsCheckerFacade.generateResults();

        assertEquals(firstCallResult, secondCallResult);
    }

}
