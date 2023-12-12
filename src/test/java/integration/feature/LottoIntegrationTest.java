package integration.feature;

import integration.BaseIntegrationTest;
import integration.apivalidationerror.ApiValidationErrorDto;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.lotto.domain.numberreceiver.dto.InputNumbersResponseDto;

import java.time.LocalDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.status;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.mock;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static pl.lotto.domain.numberreceiver.InputNumbersValidationResult.EQUALS_SIX_NUMBERS;


@Log4j2
public class LottoIntegrationTest extends BaseIntegrationTest {


    @Test
    public void should_post_input_six_numbers_with_draw_date() {
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
            InputNumbersResponseDto inputNumbersResponseDto = objectMapper.readValue(json, InputNumbersResponseDto.class);
            String ticketId = inputNumbersResponseDto.ticketUUID();
            //then
            assertAll(
                    () -> assertThat(ticketId).isNotNull(),
                    () -> assertThat(inputNumbersResponseDto.drawDate()).isEqualTo(drawDate),
                    () -> assertThat(inputNumbersResponseDto.message()).isEqualTo(EQUALS_SIX_NUMBERS.getInfo())
            );
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    @Test
    public void should_return_403_forbidden_when_request_has_incorrect_url_input_numbers() {
        //given && when
        try {
            MvcResult getInputNumbersWithNoExistingId = mockMvc.perform(post("/inputNumbers/444")
                            .content("""
                                    {
                                    "inputNumbers" : []
                                    }
                                    """.trim()
                            ).contentType(APPLICATION_JSON)
                    ).andExpect(MockMvcResultMatchers.status().isForbidden())
                    .andReturn();

            //then
            String json = getInputNumbersWithNoExistingId.getResponse().getContentAsString();
            ApiValidationErrorDto result = objectMapper.readValue(json, ApiValidationErrorDto.class);
            assertThat(result.status()).isEqualTo(FORBIDDEN);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
