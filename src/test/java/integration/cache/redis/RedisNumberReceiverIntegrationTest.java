package integration.cache.redis;

import integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numberreceiver.dto.InputNumbersRequestDto;
import pl.lotto.domain.numberreceiver.dto.TicketResponseDto;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import static integration.cache.redis.constants.RedisNumberReceiver.DRAW_DATE;
import static integration.cache.redis.constants.RedisNumberReceiver.TICKET_UUID_REGEX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static pl.lotto.domain.numberreceiver.NumberReceiverValidationResult.EQUALS_SIX_NUMBERS;

public class RedisNumberReceiverIntegrationTest extends BaseIntegrationTest {
    @Container
    private static final GenericContainer<?> REDIS;

    @SpyBean
    NumberReceiverFacade numberReceiverFacade;

    @Autowired
    CacheManager cacheManager;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

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
    public void should_save_input_numbers_to_cache_and_then_invalidate() throws Exception {
        //given && when
        Set<Integer> expectedInputNumbers = Set.of(1,2,3,4,5,6);

        ResultActions postInputNumbers = mockMvc.perform(post("/inputNumbers")
                .content("""
                        {
                            "inputNumbers" : [1, 2, 3, 4, 5, 6]
                        }
                        """.trim())
                .contentType(APPLICATION_JSON_VALUE));

        //then
        ResultActions inputNumbersResult = postInputNumbers.andExpect(status -> status(OK));
        MvcResult mvcResult = inputNumbersResult.andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        TicketResponseDto result = objectMapper.readValue(json, TicketResponseDto.class);

        assertThat(result).isNotNull();
        assertThat(result.inputNumbers()).isEqualTo(expectedInputNumbers);
        assertThat(result.ticketUUID()).containsPattern(TICKET_UUID_REGEX);
        assertThat(result.message()).isEqualTo(EQUALS_SIX_NUMBERS.getInfo());
        assertThat(result.drawDate()).isEqualTo(DRAW_DATE);
        assertThat(cacheManager.getCacheNames().contains("inputNumbers")).isTrue();
    }

    @Test
    public void should_not_save_input_numbers_to_cache_and_then_input_numbers_empty() throws Exception {
        //given && when
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
        //given
        mockMvc.perform(post("/inputNumbers")
                .content("""
                        {
                            "inputNumbers" : [1, 2, 3, 4, 5, 6]
                        }
                        """.trim())
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status -> status(200));

        mockMvc.perform(post("/inputNumbers")
                .content("""
                        {
                            "inputNumbers" : [7, 8, 9, 10, 11, 12]
                        }
                        """.trim())
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status -> status(200));

        //when
        InputNumbersRequestDto expectedInputNumbers = new InputNumbersRequestDto(Set.of(1, 2, 3, 4, 5, 6));
        verify(numberReceiverFacade, times(1)).inputNumbers(expectedInputNumbers);

        InputNumbersRequestDto expectedInputNumbers2 = new InputNumbersRequestDto(Set.of(7, 8, 9, 10, 11, 12));
        verify(numberReceiverFacade, times(1)).inputNumbers(expectedInputNumbers2);

        //then
        assertThat(cacheManager.getCacheNames().contains("inputNumbers")).isTrue();
        assertThat(Objects.requireNonNull(cacheManager.getCache("inputNumbers"))).isNotNull();
    }

    @Test
    public void should_cache_value_two_input_numbers() {

        InputNumbersRequestDto inputNumbersRequest = new InputNumbersRequestDto(Set.of(1, 2, 3, 4, 5, 6));
        TicketResponseDto ticketResponse1 = numberReceiverFacade.inputNumbers(inputNumbersRequest);

        TicketResponseDto ticketResponse2 = numberReceiverFacade.inputNumbers(inputNumbersRequest);

        assertNull(redisTemplate.opsForHash().get("inputNumbers", "result"));
        assertNull(redisTemplate.opsForHash().get("inputNumbers", "result"));
        assertEquals(ticketResponse1, ticketResponse2);

        verify(numberReceiverFacade, times(1)).inputNumbers(inputNumbersRequest);
    }
}
