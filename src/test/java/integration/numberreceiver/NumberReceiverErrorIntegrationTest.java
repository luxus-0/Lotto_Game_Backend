package integration.numberreceiver;

import com.github.tomakehurst.wiremock.client.WireMock;
import integration.BaseIntegrationTest;
import integration.dto.ApiValidationErrorDto;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.lotto.domain.numberreceiver.dto.TicketResponseDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.lotto.domain.numberreceiver.NumberReceiverValidationResult.*;

@Log4j2
public class NumberReceiverErrorIntegrationTest extends BaseIntegrationTest {

    @Test
    public void should_return_status_404_not_found_and_validation_message_when_body_has_empty_input_numbers() throws Exception {
        //give
        MvcResult perform = mockMvc.perform(post("/inputNumbers")
                        .content("""
                                {
                                "inputNumbers" : []
                                }
                                """.trim()
                        ).contentType(APPLICATION_JSON)
                ).andExpect(status().isNotFound())
                .andReturn();

        //when
        String json = perform.getResponse().getContentAsString();

        //then
        assertThrows(Exception.class, () -> {
            ApiValidationErrorDto result = objectMapper.readValue(json, ApiValidationErrorDto.class);
            result.messages().containsAll(List.of(NOT_FOUND.name(), "404"));
        });
    }

    @Test
    public void should_return_status_404_not_found_and_validation_message_when_body_is_empty() throws Exception {
        //given
        MvcResult perform = mockMvc.perform(post("/inputNumbers")
                        .content("""
                                {
                                                                   
                                 }
                                 """.trim()
                        ).contentType(APPLICATION_JSON)
                ).andExpect(status().isNotFound())
                .andReturn();

        //when
        String json = perform.getResponse().getContentAsString();

        //then
        assertThrows(Exception.class, () -> {
            ApiValidationErrorDto result = objectMapper.readValue(json, ApiValidationErrorDto.class);
            assertThat(result.messages()).containsExactlyInAnyOrder(
                    "inputNumbers must not be empty",
                    "inputNumbers must not be null");
        });
    }

    @Test
    public void should_return_status_403_forbidden_when_request_has_incorrect_url_input_numbers() throws Exception {
        //given && when
        MvcResult getInputNumbersWithNoExistingId = mockMvc.perform(post("/inputNumbers/444")
                        .content("""
                                {
                                "inputNumbers" : []
                                }
                                """.trim()
                        ).contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();

        //then
        String json = getInputNumbersWithNoExistingId.getResponse().getContentAsString();
        ApiValidationErrorDto result = new ApiValidationErrorDto(List.of("403"), FORBIDDEN);

        assertAll(
                () -> assertThrows(Exception.class,
                        () -> objectMapper.readValue(json, ApiValidationErrorDto.class)),
                () -> assertThat(result.messages()).isEqualTo(List.of("403"))
        );
    }

    @Test
    public void should_return_status_200_when_input_numbers_has_size_less_than_six() throws Exception {
        //given && when
        LocalDateTime drawDate = LocalDateTime.of(2023, 12, 16, 12, 0, 0);

        ResultActions postInputNumbersWithIncorrectSize = mockMvc.perform(post("/inputNumbers")
                .content("""
                        {
                            "inputNumbers" : [1, 2, 3, 4, 5]
                        }
                        """.trim()).contentType(APPLICATION_JSON)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE));

        MvcResult mvcResult = postInputNumbersWithIncorrectSize.andExpect(httpStatus -> WireMock.status(200)).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        TicketResponseDto ticketResponseDto = objectMapper.readValue(json, TicketResponseDto.class);
        String ticketId = ticketResponseDto.ticketUUID();

        //then
        assertAll(
                () -> assertThat(ticketId).isNotNull(),
                () -> assertThat(ticketResponseDto.drawDate()).isEqualTo(drawDate),
                () -> assertThat(ticketResponseDto.message()).isEqualTo(LESS_THAN_SIX_NUMBERS.getInfo())
        );
    }

    @Test
    public void should_return_status_200_when_input_numbers_has_size_more_than_six() throws Exception {
        //given && when
        LocalDateTime drawDate = LocalDateTime.of(2023, 12, 16, 12, 0, 0);

        ResultActions postInputNumbersWithIncorrectSize = mockMvc.perform(post("/inputNumbers")
                .content("""
                        {
                            "inputNumbers" : [1, 2, 3, 4, 5, 6, 7, 8]
                        }
                        """.trim()).contentType(APPLICATION_JSON)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE));

        MvcResult mvcResult = postInputNumbersWithIncorrectSize.andExpect(httpStatus -> WireMock.status(200)).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        TicketResponseDto ticketResponseDto = objectMapper.readValue(json, TicketResponseDto.class);
        String ticketId = ticketResponseDto.ticketUUID();

        //then
        assertAll(
                () -> assertThat(ticketId).isNotNull(),
                () -> assertThat(ticketResponseDto.drawDate()).isEqualTo(drawDate),
                () -> assertThat(ticketResponseDto.message()).isEqualTo(MORE_THAN_SIX_NUMBERS.getInfo())
        );
    }

    @Test
    public void should_return_status_200_when_input_numbers_is_out_of_range() throws Exception {
        //given && when
        LocalDateTime drawDate = LocalDateTime.of(2023, 12, 16, 12, 0, 0);

        ResultActions postInputNumbersWithIncorrectSize = mockMvc.perform(post("/inputNumbers")
                .content("""
                        {
                            "inputNumbers" : [0, 0, 0]
                        }
                        """.trim())
                .contentType(APPLICATION_JSON)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE));

        MvcResult mvcResult = postInputNumbersWithIncorrectSize.andExpect(httpStatus -> WireMock.status(200)).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        TicketResponseDto ticketResponseDto = objectMapper.readValue(json, TicketResponseDto.class);
        String ticketId = ticketResponseDto.ticketUUID();

        //then
        assertAll(
                () -> assertThat(ticketId).isNotNull(),
                () -> assertThat(ticketResponseDto.drawDate()).isEqualTo(drawDate),
                () -> assertThat(ticketResponseDto.message()).isEqualTo(OUT_OF_RANGE_NUMBERS.getInfo())
        );
    }
}
