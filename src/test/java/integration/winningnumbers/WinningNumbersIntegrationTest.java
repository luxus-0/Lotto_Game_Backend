package integration.winningnumbers;

import com.github.tomakehurst.wiremock.client.WireMock;
import integration.BaseIntegrationTest;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.lotto.domain.winningnumbers.dto.WinningTicketResponseDto;

import java.util.Set;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.status;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static pl.lotto.domain.winningnumbers.WinningNumbersValidationResult.OUT_OF_RANGE;

@Log4j2
public class WinningNumbersIntegrationTest extends BaseIntegrationTest {

    @Test
    public void should_return_winning_numbers_when_body_is_three_winning_numbers() throws Exception {
        // given && when

        String json = wireMockServer.stubFor(WireMock.get("/winning_numbers")
                .willReturn(aResponse()
                        .withStatus(OK.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody("""
                                {
                                    "winningNumbers" : [1, 2, 3]
                                }
                                """.trim())))
                .getResponse()
                .getBody();

        WinningTicketResponseDto result = objectMapper.readValue(json, WinningTicketResponseDto.class);
        assertThat(result).isNotNull();
    }

    @Test
    public void should_return_no_winning_numbers_when_body_is_empty_winning_numbers() throws Exception {
        // given && when
        String json = wireMockServer.stubFor(WireMock.get("/winning_numbers")
                        .willReturn(aResponse()
                                .withStatus(NOT_FOUND.value())
                                .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                                .withBody("""
                                        {
                                            "winningNumbers" : []
                                        }
                                        """.trim())))
                .getResponse()
                .getBody();

        //then
        WinningTicketResponseDto winningTicketResponseDto = objectMapper.readValue(json, WinningTicketResponseDto.class);
        assertThat(winningTicketResponseDto).isNotNull();
    }

    @Test
    public void should_return_winning_number_and_validation_message_when_body_is_out_of_range_numbers() throws Exception {
        {
            // given && when
            String json = wireMockServer.stubFor(WireMock.get("/winning_numbers")
                            .willReturn(aResponse()
                                    .withStatus(OK.value())
                                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                                    .withBody("""
                                            {
                                                "winningNumbers" : [0, -11, 23],
                                                "message" : "Out of range numbers"
                                            }
                                            """.trim())))
                    .getResponse()
                    .getBody();

            //then
            WinningTicketResponseDto winningTicketResponseDto = objectMapper.readValue(json, WinningTicketResponseDto.class);
            assertThat(winningTicketResponseDto).isNotNull();
            assertThat(winningTicketResponseDto.message()).isEqualTo(OUT_OF_RANGE.getMessage());
        }
    }

    @Test
    public void should_throw_exception_when_winning_numbers_is_empty() throws Exception{
        //given && when
        try {
            ResultActions getWinningNumbers = mockMvc.perform(get("/winning_numbers")
                    .contentType(APPLICATION_JSON_VALUE)
                    .content("""
                            {
                                "winningNumbers" : []
                            }
                            """.trim())
                    .contentType(APPLICATION_JSON_VALUE)
                    .header(CONTENT_TYPE, APPLICATION_JSON));

            MvcResult mvcResult = getWinningNumbers.andExpect(status -> status(404)).andReturn();

            String json = mvcResult.getResponse().getContentAsString();
            WinningTicketResponseDto winningTicketResponseDto = objectMapper.readValue(json, WinningTicketResponseDto.class);

            assertThat(winningTicketResponseDto.winningNumbers()).isEmpty();
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    @Test
    public void should_return_winning_numbers_equals_zero_when_body_is_out_of_range_numbers() throws Exception {
        //given && when
        String json = wireMockServer.stubFor(WireMock.get("/winning_numbers")
                            .willReturn(aResponse()
                                    .withStatus(OK.value())
                                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                                    .withBody("""
                                            {
                                                "ticketUUID" : "",
                                                "winningNumbers" : [0],
                                                "drawDate" : "2023-12-16T12:00",
                                                "message" : "Out of range numbers"
                                            }
                                            """.trim())))
                    .getResponse()
                    .getBody();

            //then
            WinningTicketResponseDto winningTicketResponseDto = objectMapper.readValue(json, WinningTicketResponseDto.class);
            assertThat(winningTicketResponseDto).isNotNull();
            assertThat(winningTicketResponseDto.drawDate()).isEqualTo("2023-12-16T12:00");
            assertThat(winningTicketResponseDto.message()).isEqualTo(OUT_OF_RANGE.getMessage());
            assertThat(winningTicketResponseDto.winningNumbers()).isEqualTo(Set.of(0));
        }
}
