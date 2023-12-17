package integration.numberreceiver;

import integration.BaseIntegrationTest;
import integration.dto.ApiValidationErrorDto;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
public class NumberReceiverErrorIntegrationTest extends BaseIntegrationTest {

    @Test
    public void should_return_404_not_found_and_validation_message_when_body_has_empty_input_numbers()  {
        //given && when
        try {
            MvcResult perform = mockMvc.perform(post("/inputNumbers")
                            .content("""
                                    {
                                    "inputNumbers" : []
                                    }
                                    """.trim()
                            ).contentType(APPLICATION_JSON)
                    ).andExpect(status().isNotFound())
                    .andReturn();

            //then
            String json = perform.getResponse().getContentAsString();
            ApiValidationErrorDto result = objectMapper.readValue(json, ApiValidationErrorDto.class);
            assertThat(result.messages()).containsExactlyInAnyOrder(
                    "inputNumbers must not be empty",
                    "inputNumbers must not be null");
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    @Test
    public void should_return_404_not_found_and_validation_message_when_body_is_empty() {
        //given && when
        try {
            MvcResult perform = mockMvc.perform(post("/inputNumbers")
                            .content("""
                                    {
                                                                       
                                     }
                                     """.trim()
                            ).contentType(APPLICATION_JSON)
                    ).andExpect(status().isNotFound())
                    .andReturn();

            //then
            String json = perform.getResponse().getContentAsString();
            ApiValidationErrorDto result = objectMapper.readValue(json, ApiValidationErrorDto.class);
            assertThat(result.messages()).containsExactlyInAnyOrder(
                    "inputNumbers must not be empty",
                    "inputNumbers must not be null");
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
