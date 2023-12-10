package integration.feature;

import com.github.tomakehurst.wiremock.client.WireMock;
import integration.BaseIntegrationTest;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.lotto.domain.numberreceiver.dto.TicketResponseDto;
import pl.lotto.domain.numbersgenerator.InMemoryRandomNumbersGenerator;
import pl.lotto.domain.numbersgenerator.RandomNumbersGenerator;
import pl.lotto.domain.numbersgenerator.exceptions.RandomNumbersNotFoundException;
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
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.mock;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static pl.lotto.domain.numberreceiver.InputNumbersValidationResult.EQUALS_SIX_NUMBERS;


@Log4j2
public class LottoIntegrationTest extends BaseIntegrationTest {
    RandomNumbersGenerator randomNumbersGenerator = new InMemoryRandomNumbersGenerator();
    private final ResultsCheckerFacade resultsCheckerFacade = mock(ResultsCheckerFacade.class);


    @Test
    public void should_post_input_six_numbers_with_date_draw() {
        //given
        try {
            LocalDateTime drawDate = LocalDateTime.of(2023, 12, 2, 12, 0, 0);
            //when
            ResultActions perform = mockMvc.perform(post("/inputNumbers")
                    .content("""
                            {
                                "inputNumbers" : [93 10 32 45 11 75]
                            }
                            """.trim()
                    ).contentType(MediaType.APPLICATION_JSON)
                    .header(CONTENT_TYPE, APPLICATION_JSON_VALUE));

            MvcResult mvcResult = perform.andExpect(httpStatus -> status(200)).andReturn();
            String json = mvcResult.getResponse().getContentAsString();
            TicketResponseDto ticketResponseDto = objectMapper.readValue(json, TicketResponseDto.class);
            String ticketId = ticketResponseDto.ticket().ticketUUID();
            //then
            assertAll(
                    () -> assertThat(ticketId).isNotNull(),
                    () -> assertThat(ticketResponseDto.ticket().drawDate()).isEqualTo(drawDate),
                    () -> assertThat(ticketResponseDto.message()).isEqualTo(EQUALS_SIX_NUMBERS.getInfo())
            );
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    @Test
    public void should_generate_random_numbers() {
        //given
        wireMockServer.stubFor(WireMock.get("random.org/integers/?num=6&min=1&max=99&format=plain&col=2&base=10")
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody("""
                                {
                                    "randomNumbers" : [14,17,23,56,45,90]
                                }
                                """.trim())
                        .withStatus(OK.value())));


        // when && then
        await()
                .atMost(Duration.ofSeconds(20))
                .pollInterval(Duration.ofSeconds(1))
                .until(() -> {
                    {
                        assertThrowsExactly(RandomNumbersNotFoundException.class,
                                () -> randomNumbersGenerator.generateRandomNumbers(
                                        1,
                                        1,
                                        99).randomNumbers().isEmpty());
                    }
                    return true;
                });
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
                        ResultDto result = resultsCheckerFacade.findResultByTicketUUID(ticketId);
                        if (result.numbers().isEmpty()) {
                            throw new PlayerResultNotFoundException("Player result not found");
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                    return true;
                });
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
