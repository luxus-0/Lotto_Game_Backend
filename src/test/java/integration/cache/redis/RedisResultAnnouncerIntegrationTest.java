package integration.cache.redis;

import integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.web.servlet.ResultActions;
import pl.lotto.domain.resultannouncer.ResultAnnouncerFacade;
import pl.lotto.domain.resultannouncer.ResultAnnouncerResponse;
import pl.lotto.domain.resultannouncer.dto.ResultAnnouncerResponseDto;
import pl.lotto.domain.resultchecker.ResultsCheckerFacade;
import pl.lotto.domain.resultchecker.WinningTicket;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

import static com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.HttpHeaders.CONTENT_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static pl.lotto.domain.resultchecker.ResultCheckerMessageProvider.WIN;

public class RedisResultAnnouncerIntegrationTest extends BaseIntegrationTest {

    @SpyBean
    ResultAnnouncerFacade resultAnnouncerFacade;
    @SpyBean
    ResultsCheckerFacade resultsCheckerFacade;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    CacheManager cacheManager;
    @Test
    public void should_save_result_to_cache_and_then_invalidate_by_one_times_of_invocations() throws Exception {
        //given && then

        clock.withZone(ZoneId.systemDefault());
        String ticketUUID = "550e8400-e29b-41d4-a716-446655440000";

        resultCheckerRepository.saveAll(List.of(WinningTicket.builder()
                .ticketUUID(ticketUUID)
                .inputNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3))
                .isWinner(true)
                .drawDate(LocalDateTime.now())
                .message(WIN)
                .build()));

        resultAnnouncerRepository.save(ResultAnnouncerResponse.builder()
                .ticketUUID(ticketUUID)
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3))
                .isWinner(true)
                .drawDate(LocalDateTime.now())
                .message(WIN)
                .build());

        ResultActions getResults = mockMvc.perform(get("/results/" + ticketUUID)
                .content("""
                        {
                            "ticketUUID" : "550e8400-e29b-41d4-a716-446655440000"
                        }
                        """.trim())
                .contentType(APPLICATION_JSON_VALUE));

        //then
        getResults.andExpect(status -> status(OK));

        verify(resultAnnouncerFacade, atMost(1)).findResultAnnouncer(ticketUUID);
        assertThat(cacheManager.getCacheNames().contains("results")).isTrue();
    }

    @Test
    public void should_cache_results_and_validated_with_wait_seconds() {
        String ticketUUID = "550e8400-e29b-41d4-a716-446655440000";

        resultCheckerRepository.saveAll(List.of(WinningTicket.builder()
                .ticketUUID(ticketUUID)
                .inputNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3))
                .isWinner(true)
                .drawDate(LocalDateTime.now())
                .message(WIN)
                .build()));

        resultAnnouncerRepository.save(ResultAnnouncerResponse.builder()
                .ticketUUID(ticketUUID)
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3))
                .isWinner(true)
                .drawDate(LocalDateTime.now())
                .message(WIN)
                .build());

        await()
                .atMost(Duration.ofSeconds(4))
                .pollInterval(Duration.ofSeconds(1))
                .untilAsserted(() -> {
                    mockMvc.perform(get("/results/" + ticketUUID)
                            .header(CONTENT_TYPE, "application/json")
                            .contentType(APPLICATION_JSON_VALUE));

                    verify(resultAnnouncerFacade, atLeast(1)).findResultAnnouncer(ticketUUID);
                });
    }

    @Test
    public void should_save_result_to_cache_and_then_incorrect_cache_name() throws Exception {
        //given && then
        String ticketUUID = "550e8400-e29b-41d4-a716-446655440000";

        resultCheckerRepository.saveAll(List.of(WinningTicket.builder()
                .ticketUUID(ticketUUID)
                .inputNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3))
                .isWinner(true)
                .drawDate(LocalDateTime.now())
                .message(WIN)
                .build()));

        resultAnnouncerRepository.save(ResultAnnouncerResponse.builder()
                .ticketUUID(ticketUUID)
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3))
                .isWinner(true)
                .drawDate(LocalDateTime.now())
                .message(WIN)
                .build());

        ResultActions getResults = mockMvc.perform(get("/results/" + ticketUUID)
                .content("""
                        {
                            "ticketUUID" : "550e8400-e29b-41d4-a716-446655440000"
                        }
                        """.trim())
                .contentType(APPLICATION_JSON_VALUE));

        //then
        getResults.andExpect(status -> status(OK));

        assertThat(cacheManager.getCacheNames().contains("result")).isFalse();
    }

    @Test
    public void should_save_result_to_cache_and_then_one_times_invocations() throws Exception {
        String ticketUUID = "123";
        resultCheckerRepository.saveAll(List.of(WinningTicket.builder()
                .ticketUUID(ticketUUID)
                .inputNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3))
                .isWinner(true)
                .drawDate(LocalDateTime.now())
                .message(WIN)
                .build()));

        resultAnnouncerRepository.save(ResultAnnouncerResponse.builder()
                .ticketUUID(ticketUUID)
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3))
                .isWinner(true)
                .drawDate(LocalDateTime.now())
                .message(WIN)
                .build());

        ResultAnnouncerResponseDto result1 = resultAnnouncerFacade.findResultAnnouncer(ticketUUID);

        assertNull(redisTemplate.opsForValue().get("results/" + ticketUUID));

        // Call the method for the second time
        ResultAnnouncerResponseDto result2 = resultAnnouncerFacade.findResultAnnouncer(ticketUUID);

        assertNull(redisTemplate.opsForValue().get("results/" + ticketUUID));

        assertEquals(result1, result2);

        verify(resultsCheckerFacade, times(1)).findResultByTicketUUID(ticketUUID);
        verify(resultsCheckerFacade, atLeast(1)).findResultByTicketUUID(ticketUUID);
    }
}
