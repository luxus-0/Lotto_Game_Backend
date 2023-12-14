package integration.resultchecker;

import integration.BaseIntegrationTest;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import pl.lotto.domain.resultchecker.ResultsCheckerFacade;

import java.time.Duration;

import static com.github.tomakehurst.wiremock.client.WireMock.status;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@Log4j2
public class ResultsCheckerErrorsIntegrationTest extends BaseIntegrationTest {

    private final ResultsCheckerFacade resultsCheckerFacade = mock(ResultsCheckerFacade.class);

    @Test
    public void should_return_result_with_sample_ticket_with_correct_draw_date() {
        // given
        String ticketUUID = "1234567";
        try {

            mockMvc.perform(post("inputNumbers")
                            .contentType(APPLICATION_JSON))
                            .andExpect(status -> status(200)
                                    .withBody("""
                                            "inputNumbers": [1,2,3,4,5,6]
                                            """));


            mockMvc.perform(get("/results/" + ticketUUID)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status -> status(404))
                    .andExpect(content().json("""
                            {
                            "message" : "TICKET NOT FOUND",
                            "status" : "NOT_FOUND"
                            }
                                """.trim()
                    ));

            //when
            //then
            await()
                    .atMost(Duration.ofSeconds(20))
                    .pollInterval(Duration.ofSeconds(1))
                    .until(() ->
                    {
                        assertThatThrownBy(() -> resultsCheckerFacade.findResultByTicketUUID(ticketUUID));
                        return true;
                    });
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


    @Test
    public void should_throw_status_404_not_found_and_body_with_message_not_found_when_ticketUUID_is_not_existing() {
        // given && when
        clock.plusMinutes(6);

        try {
            String notExistingTicketId = "12345";

            mockMvc.perform(get("/results/" + notExistingTicketId))
                    .andExpect(status -> status(404))
                    .andExpect(content().json("""
                            {
                            "message" : "Not found for ticketPlayer id: 1234"
                            "status: "NOT_FOUND"
                            }
                                """.trim()
                    ));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        }
    }
