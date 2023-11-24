package integration;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.RestTemplate;
import pl.lotto.domain.numberreceiver.dto.TicketResponseDto;
import pl.lotto.domain.numbersgenerator.WinningTicketFacade;
import pl.lotto.domain.numbersgenerator.exceptions.WinningNumbersNotFoundException;
import pl.lotto.domain.numbersgenerator.dto.WinningTicketDto;
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


@Log4j2
public class LottoIntegrationTest extends BaseIntegrationTest {

    @Autowired
    WinningTicketFacade winningTicketFacade;
    @Autowired
    ResultsCheckerFacade resultsCheckerFacade;
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
                                [1 2 3 4 5 6 82 83 57 10 81 34]
                                """.trim())));
        //when && then
        LocalDateTime drawDate = LocalDateTime.of(2022, 11, 19, 12, 0, 0);
        await()
                .atMost(Duration.ofSeconds(20))
                .pollInterval(Duration.ofSeconds(1))
                .until(() -> {
                    try {
                        return !winningTicketFacade.retrieveWinningNumbersByDate(drawDate).winningNumbers().isEmpty();

                    } catch (WinningNumbersNotFoundException e) {
                        return false;
                    }
                });
    }

    @Test
    public void should_post_input_six_numbers_with_date_draw() throws Exception {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2022, 11, 19, 12, 0, 0);
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
        TicketResponseDto ticketResponseDto = objectMapper.readValue(json, TicketResponseDto.class);
        String ticketId = ticketResponseDto.ticketDto().ticketId();
        //then
        assertAll(
                () -> assertThat(ticketId).isNotNull(),
                () -> assertThat(ticketResponseDto.ticketDto().drawDate()).isEqualTo(drawDate),
                () -> assertThat(ticketResponseDto.message()).isEqualTo("equals six numbers")
        );
    }

    @Test
    public void should_return_result_with_sample_ticket_with_correct_draw_date() {
        // given && when && then
        String ticketId = "1234567";

        clock.plusDaysAndMinutes(3, 55);

        await()
                .atMost(30, TimeUnit.SECONDS)
                .pollInterval(Duration.ofSeconds(10L))
                .until(() -> {
                            try {
                                ResultDto result = resultsCheckerFacade.findResultByTicketId(ticketId);
                                return result.numbers().isEmpty();
                            } catch (PlayerResultNotFoundException exception) {
                                return false;
                            }
                        }
                );
        clock.plusMinutes(6);
    }

    @Test
    public void should_return_status_404_with_no_ticket_id() throws Exception {
        // given && when
        String ticketId = "12345";

        ResultActions performGetMethod = mockMvc.perform(get("/results/12345"));

        // then
        MvcResult mvcResultGetMethod = performGetMethod.andExpect(result -> status(404)).andReturn();
        String jsonGetMethod = mvcResultGetMethod.getResponse().getContentAsString();
        ResultAnnouncerResponseDto finalResult = objectMapper.readValue(jsonGetMethod, ResultAnnouncerResponseDto.class);

        assertThat(finalResult.message()).isEqualTo("Player result not found");

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
                                                WinningTicketDto.class)
                                        .getBody()));
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
                                                WinningTicketDto.class)
                                        .getBody()));
    }
}
