package integration.numberreceiver;

import integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.lotto.domain.numberreceiver.dto.TicketResponseDto;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static com.github.tomakehurst.wiremock.client.WireMock.status;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static pl.lotto.domain.numberreceiver.NumberReceiverValidationResult.EQUALS_SIX_NUMBERS;

public class NumberReceiverIntegrationTest extends BaseIntegrationTest {
    @Test
    public void should_post_input_six_numbers_with_draw_date() throws Exception {
        //given
            LocalDateTime drawDate = LocalDateTime.now(ZoneId.systemDefault()).withHour(12).withMinute(0).withSecond(0).withNano(0);
            //when
            ResultActions perform = mockMvc.perform(post("/inputNumbers")
                    .content("""
                            {
                                "inputNumbers" : [93,10, 32, 45, 11, 75]
                            }
                            """.trim()
                    ).contentType(MediaType.APPLICATION_JSON)
                    .header(CONTENT_TYPE, APPLICATION_JSON_VALUE));

            MvcResult mvcResult = perform.andExpect(httpStatus -> status(200)).andReturn();
            String json = mvcResult.getResponse().getContentAsString();
            TicketResponseDto ticketResponseDto = objectMapper.readValue(json, TicketResponseDto.class);
            String ticketId = ticketResponseDto.ticketUUID();
            //then
            assertAll(
                    () -> assertThat(ticketId).isNotNull(),
                    () -> assertThat(ticketResponseDto.drawDate()).isEqualTo(drawDate),
                    () -> assertThat(ticketResponseDto.message()).isEqualTo(EQUALS_SIX_NUMBERS.getInfo())
            );
    }
}
