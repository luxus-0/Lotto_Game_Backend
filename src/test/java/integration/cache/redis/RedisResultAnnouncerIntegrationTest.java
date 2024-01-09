package integration.cache.redis;

import integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import pl.lotto.domain.resultannouncer.ResultAnnouncerFacade;

import java.time.Duration;

import static com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.HttpHeaders.CONTENT_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class RedisResultAnnouncerIntegrationTest extends BaseIntegrationTest {
    @Container
    private static final GenericContainer<?> REDIS;

    @SpyBean
    ResultAnnouncerFacade resultAnnouncerFacade;

    @Autowired
    CacheManager cacheManager;

    static {
        REDIS = new GenericContainer<>("redis").withExposedPorts(6379);
        REDIS.start();
    }

    @Test
    public void should_save_result_to_cache_and_then_invalidate_by_one_times_of_invocations() throws Exception {
        //given && then
        String ticketUUID = "550e8400-e29b-41d4-a716-446655440000";

        ResultActions getResults = mockMvc.perform(get("/results/" +ticketUUID)
                .content("""
                        {
                            "ticketUUID" : "550e8400-e29b-41d4-a716-446655440000"
                        }
                        """.trim())
                .contentType(APPLICATION_JSON_VALUE));

        //then
        getResults.andExpect(status -> status(OK));

        verify(resultAnnouncerFacade, times(1)).findResult("550e8400-e29b-41d4-a716-446655440000");
        assertThat(cacheManager.getCacheNames().contains("results")).isTrue();
    }

    @Test
    public void should_cache_results_and_validated_with_wait_seconds() {
        String ticketUUID = "550e8400-e29b-41d4-a716-446655440000";

        await()
                .atMost(Duration.ofSeconds(4))
                .pollInterval(Duration.ofSeconds(1))
                .untilAsserted(() -> {
                    mockMvc.perform(get("/results/" + ticketUUID)
                            .header(CONTENT_TYPE, "application/json")
                            .contentType(APPLICATION_JSON_VALUE));

                    verify(resultAnnouncerFacade, atLeast(1)).findResult(ticketUUID);
                });
    }

    @Test
    public void should_save_result_to_cache_and_then_incorrect_cache_name() throws Exception {
        //given && then
        String ticketUUID = "550e8400-e29b-41d4-a716-446655440000";

        ResultActions getResults = mockMvc.perform(get("/results/" +ticketUUID)
                .content("""
                        {
                            "ticketUUID" : "550e8400-e29b-41d4-a716-446655440000"
                        }
                        """.trim())
                .contentType(APPLICATION_JSON_VALUE));

        //then
        getResults.andExpect(status -> status(OK));

        verify(resultAnnouncerFacade, times(1)).findResult("550e8400-e29b-41d4-a716-446655440000");
        assertThat(cacheManager.getCacheNames().contains("result")).isFalse();
    }
}
