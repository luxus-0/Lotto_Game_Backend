package integration.cache.redis;

import integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numberreceiver.dto.InputNumbersRequestDto;
import pl.lotto.domain.numberreceiver.dto.TicketResponseDto;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class RedisNumberReceiverIntegrationTest extends BaseIntegrationTest {
    @Container
    private static final GenericContainer<?> REDIS;

    @SpyBean
    NumberReceiverFacade numberReceiverFacade;

    @Autowired
    CacheManager cacheManager;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    static {
        REDIS = new GenericContainer<>("redis").withExposedPorts(6379);
        REDIS.start();
    }
    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.data.redis.port", () -> REDIS.getFirstMappedPort().toString());
        registry.add("spring.cache.type", () -> "redis");
        registry.add("spring.cache.redis.time-to-live", () -> "PT1S");
    }

    @Test
    public void should_save_input_numbers_to_cache_and_then_invalidate_by_time_to_live() throws Exception {
        //given && then
        ResultActions postInputNumbers = mockMvc.perform(post("/inputNumbers")
                .content("""
                        {
                            "inputNumbers" : [1, 2, 3, 4, 5, 6]
                        }
                        """.trim())
                .contentType(APPLICATION_JSON_VALUE));


        //then
        postInputNumbers.andExpect(status -> status(OK));

        verify(numberReceiverFacade, times(1)).inputNumbers(new InputNumbersRequestDto(Set.of(1,2,3,4,5,6)));
        assertThat(cacheManager.getCacheNames().contains("inputNumbers")).isTrue();
    }

    @Test
    public void should_not_save_input_numbers_to_cache_and_then_input_numbers_empty() throws Exception {
        //given && then
        ResultActions postInputNumbers = mockMvc.perform(post("/inputNumbers")
                .content("""
                        {
                            "inputNumbers" : []
                        }
                        """.trim())
                .contentType(APPLICATION_JSON_VALUE));


        //then
        postInputNumbers.andExpect(status -> status(OK));

        verify(numberReceiverFacade, times(1)).inputNumbers(new InputNumbersRequestDto(Collections.EMPTY_SET));
        assertThat(cacheManager.getCacheNames().contains("inputNumbers")).isTrue();
    }

    @Test
    public void should_save_two_input_numbers_to_cache_and_then_invalidate_by_time_to_live() throws Exception {
        //given && then
        ResultActions postInputNumbers = mockMvc.perform(post("/inputNumbers")
                .content("""
                        {
                            "inputNumbers" : [1, 2, 3, 4, 5, 6]
                        }
                        """.trim())
                .contentType(APPLICATION_JSON_VALUE));

        ResultActions postInputNumbers2 = mockMvc.perform(post("/inputNumbers")
                .content("""
                        {
                            "inputNumbers" : [7, 8, 9, 10, 11, 12]
                        }
                        """.trim())
                .contentType(APPLICATION_JSON_VALUE));


        //then
        postInputNumbers.andExpect(status -> status(OK));
        postInputNumbers2.andExpect(status -> status(OK));

        verify(numberReceiverFacade, times(1)).inputNumbers(new InputNumbersRequestDto(Set.of(1,2,3,4,5,6)));
        verify(numberReceiverFacade, times(1)).inputNumbers(new InputNumbersRequestDto(Set.of(7,8,9,10,11,12)));
        assertThat(cacheManager.getCacheNames().contains("inputNumbers")).isTrue();
    }

    @Test
    public void should_cache_value_two_input_numbers() {

        InputNumbersRequestDto inputNumbersRequest = new InputNumbersRequestDto(Set.of(1, 2, 3, 4, 5, 6));
        TicketResponseDto ticketResponse1 = numberReceiverFacade.inputNumbers(inputNumbersRequest);

        assertNull(getCachedValue("inputNumbers", "result"));

        // Call the method for the second time
        TicketResponseDto ticketResponse2 = numberReceiverFacade.inputNumbers(inputNumbersRequest);

        // Ensure that the result is retrieved from the cache
        assertNull(getCachedValue("inputNumbers", "result"));

        assertEquals(ticketResponse1, ticketResponse2);

        verify(numberReceiverFacade, times(1)).inputNumbers(inputNumbersRequest);
    }

    public Object getCachedValue(String key, String hashKey) {
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        return hashOperations.get(key, hashKey);
    }
}
