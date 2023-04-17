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
import pl.lotto.domain.resultannouncer.dto.ResultAnnouncerResponseDto;
import pl.lotto.domain.resultchecker.ResultsCheckerFacade;
import pl.lotto.domain.resultchecker.dto.ResultDto;
import pl.lotto.domain.resultchecker.exceptions.PlayerResultNotFoundException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


public class LottoIntegrationTest extends BaseIntegrationTest {

    @Autowired
    WinningNumbersFacade winningNumbersFacade;
    @Autowired
    ResultsCheckerFacade resultsCheckerFacade;
    @Autowired
    RestTemplate restTemplate;

    @Test
    public void should_user_win_and_generate_winners() throws Exception {
        // step 1: external service returns 6 random numbers (1,2,3,4,5,6)
        //given
        wireMockServer.stubFor(WireMock.get("random.org/integers/?num=6&min=1&max=99&format=plain&col=1&base=10")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                [1 2 3 4 5 6 23 56 78 89 90 43 65 87 40 11]
                                """.trim())));
        //step 2: system fetched winning numbers for draw date: 19.11.2022 12:00
        //when && then
        LocalDateTime drawDate = LocalDateTime.of(2022, 11, 19, 12, 0, 0);
        await()
                .pollInterval(Duration.ofSeconds(1))
                .until(() -> {
                    try {
                        return !winningNumbersFacade.retrieveWinningNumbersByDate(drawDate).winningNumbers().isEmpty();

                    } catch (WinningNumbersNotFoundException e) {
                        return false;
                    }
                });

        //step 3: user made POST /inputNumbers with 6 numbers (1, 2, 3, 4, 5, 6) at 16-11-2022 10:00 and system returned OK(200) with message: “equals six numbers” and Ticket (DrawDate:19.11.2022 12:00 (Saturday), TicketId: sampleTicketId)
        //given
        //when
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
        String ticketId = ticketResultDto.ticketDto().hash();
        //then
        assertAll(
                () -> assertThat(ticketId).isNotNull(),
                () -> assertThat(ticketResultDto.ticketDto().drawDate()).isEqualTo(drawDate),
                () ->assertThat(ticketResultDto.message()).isEqualTo("equals six numbers")
        );

        //step 4: user made GET /results/notExistingId and system returned 404(NOT_FOUND) and body with (message: Not found for id: notExistingId and status NOT_FOUND)
        // given
        // when
        ResultActions resultsWithNoExistingId = mockMvc.perform(get("/results/notExistingId"));
        //then
        resultsWithNoExistingId.andExpect(result -> status(404))
                .andExpect(content().json(
                        """
                        {
                            "message": "Not found for id: notExistingId",
                            "status": "NOT_FOUND"
                        }
                        """.trim()
                ));

        //step 5: 3 days and 55 minutes passed, and it is 5 minute before draw (19.11.2022 11:55)
        // given && when && then
        clock.plusDaysAndMinutes(3, 55);


        //step 6: system generated result for TicketId: sampleTicketId with draw date 19.11.2022 12:00, and saved it with 6 hits
        await().atMost(20, TimeUnit.SECONDS)
                .pollInterval(Duration.ofSeconds(1L))
                .until(() -> {
                            try {
                                ResultDto result = resultsCheckerFacade.findByTicketId(ticketId);
                                return !result.numbers().isEmpty();
                            } catch (PlayerResultNotFoundException exception) {
                                return false;
                            }
                        }
                );

        //step 7: 6 minutes passed it is 1 minute after the draw (19.11.2022 12:01)
        clock.plusMinutes(6);


        //step 8: ser made GET /results/sampleTicketId and system returned 200 (OK)
        // given && when
        ResultActions performGetMethod = mockMvc.perform(get("/results/" + ticketId));

        // then
        MvcResult mvcResultGetMethod = performGetMethod.andExpect(result -> status(200)).andReturn();
        String jsonGetMethod = mvcResultGetMethod.getResponse().getContentAsString();
        ResultAnnouncerResponseDto finalResult = objectMapper.readValue(jsonGetMethod, ResultAnnouncerResponseDto.class);
        assertAll(
                () -> assertThat(finalResult.message()).isEqualTo("Congratulations, you won!"),
                () -> assertThat(finalResult.resultDto().hash()).isEqualTo(ticketId),
                () -> assertThat(finalResult.resultDto().hitNumbers()).hasSize(6));

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
}
