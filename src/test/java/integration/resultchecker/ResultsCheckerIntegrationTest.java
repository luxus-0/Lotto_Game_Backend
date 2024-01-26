package integration.resultchecker;

import integration.BaseIntegrationTest;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.lotto.domain.resultannouncer.ResultAnnouncerResponse;
import pl.lotto.domain.winningnumbers.WinningTicket;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static com.github.tomakehurst.wiremock.client.WireMock.status;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@Log4j2
public class ResultsCheckerIntegrationTest extends BaseIntegrationTest {

    @Test
    public void should_return_message_and_status_not_found_when_results_id_is_incorrect() {
        // given && when && then
        try {

            String notExistingTicketId = "12345";

            mockMvc.perform(get("/results/" + notExistingTicketId)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status -> status(404))
                    .andExpect(content().json("""
                            {
                            "message" : "TICKET NOT FOUND",
                            "status" : "Not found for winningTicket uuid: 1234567"
                            }
                                """.trim()
                    )).andDo(print()).andReturn();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


    @Test
    public void should_return_message_and_status_not_found_when_results_id_is_incorrect_v2() {
        // given && when

        String notExistingTicketId = "12345";

        try {
            ResultActions getResultWithNotExistingId = mockMvc.perform(get("/results/" + notExistingTicketId))
                    .andExpect(content().json("""
                            {
                            "message" : "Not found for winning ticket uuid: 12345"
                            "status: "NOT_FOUND"
                            }
                                """.trim()
                    ));

            MvcResult mvcResult = getResultWithNotExistingId.andReturn();
            String json = mvcResult.getResponse().getContentAsString();

            assertThat(json).isEmpty();

        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    @Test
    public void should_return_correct_result_when_saved_to_database() throws Exception {

        String expectedTicketUUID = "12345";
        Set<Integer> expectedInputNumbers = Set.of(1, 2, 3, 4, 5, 6);
        Set<Integer> expectedHitNumbers = Set.of(1, 2, 3);
        LocalDateTime expectedDrawDate = LocalDateTime.of(2024, 11, 19, 12, 0, 0);
        boolean isWinner = true;

        ResultAnnouncerResponse expectedAnnouncerResult = ResultAnnouncerResponse.builder()
                .ticketUUID(expectedTicketUUID)
                .inputNumbers(expectedInputNumbers)
                .hitNumbers(expectedHitNumbers)
                .drawDate(expectedDrawDate)
                .isWinner(isWinner)
                .message("WIN")
                .build();

        WinningTicket expectedWinningTicketResult = WinningTicket.builder()
                .ticketUUID(expectedTicketUUID)
                .inputNumbers(expectedInputNumbers)
                .hitNumbers(expectedHitNumbers)
                .drawDate(expectedDrawDate)
                .isWinner(isWinner)
                .message("WIN")
                .build();


        resultAnnouncerRepository.save(expectedAnnouncerResult);
        resultCheckerRepository.saveAll(List.of(expectedWinningTicketResult));

        ResultActions getResults = mockMvc.perform(get("/results/" + expectedTicketUUID))
                .andExpect(status -> status(200))
                .andExpect(content()
                        .json("""
                        {
                           "ticketUUID": "12345",
                            "inputNumbers": [1, 2, 3, 4, 5, 6],
                             "hitNumbers": [1, 2, 3],
                             "drawDate": "2024-11-19T12:00:00",
                              "isWinner": true,
                              "message": "WIN"
                        }
                            """.trim()));

        MvcResult mvcResult = getResults.andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        ResultAnnouncerResponse result = objectMapper.readValue(json, ResultAnnouncerResponse.class);

        assertThat(result).isNotNull();
    }
}
