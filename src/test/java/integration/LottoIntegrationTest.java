package integration;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.RestTemplate;
import pl.lotto.domain.numberreceiver.dto.TicketResultDto;
import pl.lotto.domain.numbersgenerator.WinningNumbersFacade;
import pl.lotto.domain.numbersgenerator.WinningNumbersNotFoundException;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


public class LottoIntegrationTest extends BaseIntegrationTest {

    @Autowired
    WinningNumbersFacade winningNumbersFacade;
    @Autowired
    RestTemplate restTemplate;

    @Test
    public void should_user_win_and_generate_winners() {
        //given
        wireMockServer.stubFor(WireMock.get("random.org/integers/?num=6&min=1&max=99&format=plain&col=1&base=10")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                [1 2 3 4 5 6 23 56 78 89 90 43 65 87 40 11]
                                """.trim())));
        //when
        LocalDateTime drawDate = LocalDateTime.of(2022, 11, 19, 12, 0, 0);
        //then
        await()
                .atMost(Duration.ofSeconds(10))
                .pollInterval(Duration.ofSeconds(1))
                .until(() -> {
                    try {
                        return !winningNumbersFacade.retrieveWinningNumbersByDate(drawDate).winningNumbers().isEmpty();

                    } catch (WinningNumbersNotFoundException e) {
                        return false;
                    }
                });
    }

    @Test
    public void should_return_generate_six_random_numbers_with_correct_range() {
        //given
        StringValuePattern countPattern = equalTo("6");
        StringValuePattern rangeFromPattern = equalTo("1");
        StringValuePattern rangeToPattern = equalTo("99");

        wireMockServer.stubFor(WireMock.get(urlMatching("/integers.*"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("1\n4\n6\n8\n10\n12\n"))
                .withQueryParam("num", countPattern)
                .withQueryParam("min", rangeFromPattern)
                .withQueryParam("max", rangeToPattern));
        //when
        ResponseEntity<String> response = restTemplate.getForEntity("https://random.org/integers/?num=6&min=1&max=99&format=plain&col=1&base=10", String.class);
        //then
        assertThat(response.getBody()).isNotEmpty();
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void ShouldThrowingExceptionWhenUrlIsIncorrect() {
        //given
        wireMockServer.stubFor(WireMock.get(urlEqualTo("/integers.*"))
                .willReturn(aResponse()
                        .withHeader("Media-Type", "application/json")
                        .withBody("")
                        .withStatus(500)));
        //when
        //then
        assertThrows(IllegalArgumentException.class,
                () -> Objects.requireNonNull(
                                restTemplate.exchange("/integers?num=6&min=1&max=99",
                                                HttpMethod.GET,
                                                null,
                                                WinningNumbersDto.class)
                                        .getBody())
                        .winningNumbers());
    }

    @Test
    public void ShouldThrowingExceptionWhenUrlIsEmpty() {
        //given
        wireMockServer.stubFor(WireMock.get(urlEqualTo("/integers.*"))
                .willReturn(aResponse()
                        .withHeader("Media-Type", "application/json")
                        .withBody("")
                        .withStatus(500)));
        //when
        //then
        assertThrows(IllegalArgumentException.class,
                () -> Objects.requireNonNull(
                                restTemplate.exchange("",
                                                HttpMethod.GET,
                                                null,
                                                WinningNumbersDto.class)
                                        .getBody())
                        .winningNumbers());
    }

    @Test
    public void should_return_saved_six_input_numbers() throws Exception {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2022, 11, 19, 12, 0, 0);

        ResultActions perform = mockMvc.perform(post("/inputNumbers")
                .content("""
                        {
                        "inputNumbers": [1, 2, 3, 4, 5, 6]
                        }
                        """.trim()
                ).contentType(MediaType.APPLICATION_JSON)
        );
        //when
        MvcResult mvcResult = perform.andExpect(status -> status(200)).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        TicketResultDto ticketResultDto = objectMapper.readValue(json, TicketResultDto.class);
        //then
       assertThat(ticketResultDto.ticketDto().drawDate()).isEqualTo(drawDate);
       assertThat(ticketResultDto.message()).isEqualTo("equals six numbers");
    }

    @Test
    public void should_return_input_numbers_out_of_bounds() throws Exception {
        //given

        ResultActions perform = mockMvc.perform(post("/inputNumbers")
                .content("""
                        {
                        "inputNumbers": [100, 2, 3, 4, 5, 200]
                        }
                        """.trim()
                ).contentType(MediaType.APPLICATION_JSON)
        );
        //when
        MvcResult mvcResult = perform.andExpect(status -> status(200)).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        TicketResultDto ticketResultDto = objectMapper.readValue(json, TicketResultDto.class);
        //then
        assertAll(
                () -> assertThat(ticketResultDto.ticketDto()).isNull(),
                () -> assertThat(ticketResultDto.message()).isEqualTo("out of range numbers")
        );
    }

    @Test
    public void should_throw_exception_when_results_are_not_exist_id() throws Exception {
        //given
        ResultActions resultsWithNoExistingId = mockMvc.perform(get("/results/notExistingId"));
        //when && then
        resultsWithNoExistingId.andExpect(result -> status(404))
                .andExpect(content().json(
                """
                {
                    "message": "Not found for id: notExistingId",
                    "status": "NOT_FOUND"
                }
                """.trim()
                ));

    }
}
