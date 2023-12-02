package integration;

import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.lotto.domain.numberreceiver.dto.TicketResponseDto;
import pl.lotto.domain.numbersgenerator.WinningNumbersFacade;
import pl.lotto.domain.numbersgenerator.exceptions.WinningNumbersNotFoundException;
import pl.lotto.domain.resultchecker.ResultsCheckerFacade;
import pl.lotto.domain.resultchecker.dto.ResultDto;
import pl.lotto.domain.resultchecker.exceptions.PlayerResultNotFoundException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.status;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static pl.lotto.domain.numberreceiver.TicketValidationResult.EQUALS_SIX_NUMBERS;


@Log4j2
public class LottoIntegrationTest extends BaseIntegrationTest {


    @Autowired
    private WinningNumbersFacade winningNumbersFacade;
    @Autowired
    private ResultsCheckerFacade resultsCheckerFacade;

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
                        return !winningNumbersFacade.retrieveWinningNumbersByDate(drawDate).winningNumbers().isEmpty();
                    } catch (WinningNumbersNotFoundException e) {
                        return true;
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

        MvcResult mvcResult = perform.andExpect(status -> status(200)).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        TicketResponseDto ticketResponseDto = objectMapper.readValue(json, TicketResponseDto.class);
        String ticketId = ticketResponseDto.ticket().ticketId();
        //then
        assertAll(
                () -> assertThat(ticketId).isNotNull(),
                () -> assertThat(ticketResponseDto.ticket().drawDate()).isEqualTo(drawDate),
                () -> assertThat(ticketResponseDto.message()).isEqualTo(EQUALS_SIX_NUMBERS.getInfo())
        );
    }

    @Test
    public void should_return_result_with_sample_ticket_with_correct_draw_date() {
        // given
        String ticketId = "1234567";

        //when
        clock.plusDaysAndMinutes(3, 55);

        //then
        await()
                .atMost(30, TimeUnit.SECONDS)
                .pollInterval(Duration.ofSeconds(10L))
                .until(() -> {
                            try {
                                ResultDto result = resultsCheckerFacade.findResultByTicketId(ticketId);
                                return !result.numbers().isEmpty();
                            } catch (PlayerResultNotFoundException exception) {
                                return false;
                            }
                        }
                );
    }

    @Test
    public void should_return_status_404_not_found_and_body_with_message_not_found_for_ticket_id_nonExistingTicketId() {
        // given && when
        clock.plusMinutes(6);

        try {
            String notExistingTicketId = "12345";

            mockMvc.perform(get("/results/" + notExistingTicketId))
                    .andExpect(status -> status(404))
                    .andExpect(content().json("""
                            {
                            "message" : "Not found for ticket id: 1234"
                            "status: "NOT_FOUND"
                            }
                                """.trim()
                    ));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
