package integration.numberreceiver;

import integration.BaseIntegrationTest;
import integration.dto.ApiValidationErrorDto;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
public class NumberReceiverErrorIntegrationTest extends BaseIntegrationTest {

    @Test
    public void should_return_404_not_found_and_validation_message_when_body_has_empty_input_numbers() throws Exception {
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
    public void should_return_404_not_found_and_validation_message_when_body_is_empty() throws Exception {
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
    public void should_return_403_forbidden_when_request_has_incorrect_url_input_numbers() throws Exception {
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
    public void should_return_400_bad_request_when_input_numbers_has_size_less_than_six() throws Exception {
        MvcResult getInputNumbersWithIncorrectSize = mockMvc.perform(post("/inputNumbers")
                .content("""
                                "inputNumbers" : [1, 2, 3, 4, 5]
                                """.trim()).contentType(APPLICATION_JSON)
                ).andExpect(status().isBadRequest())
                .andReturn();

        String json = getInputNumbersWithIncorrectSize.getResponse().getContentAsString();

        assertAll(
                () -> assertThrows(Exception.class,
                        () -> objectMapper.readValue(json, ApiValidationErrorDto.class),
                        BAD_REQUEST.name()));
    }
}
