package integration.apivalidationerror;

import integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.ResponseEntity.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class ApiValidationFailIntegrationTest extends BaseIntegrationTest {

    @Test
    public void should_return_400_bad_request_and_validation_message_when_input_numbers_are_empty() throws Exception {
        //given && when
        ResultActions emptyInputNumbers = mockMvc.perform(post("/inputNumbers")
                .content("""
                        {
                        "inputNumbers": []
                        }
                        """.trim()
                ).contentType(MediaType.APPLICATION_JSON)
        );
        //then
        MvcResult mvcResult = emptyInputNumbers.andExpect(result -> status(400)).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        ApiValidationErrorDto response = objectMapper.readValue(json, ApiValidationErrorDto.class);
        assertThat(response.messages()).containsExactlyInAnyOrder(
                "Input numbers must not be empty");
    }
    @Test
    public void should_return_400_bad_request_and_validation_message_when_does_not_have_input_numbers() throws Exception {
        //given && when
        ResultActions notHaveInputNumbers = mockMvc.perform(post("/inputNumbers")
                .content("""
                        {
                        }
                        """.trim()
                ).contentType(MediaType.APPLICATION_JSON)
        );
        //then
        MvcResult mvcResult = notHaveInputNumbers.andExpect(result -> status(400)).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        ApiValidationErrorDto response = objectMapper.readValue(json, ApiValidationErrorDto.class);
        assertThat(response.messages()).containsExactlyInAnyOrder(
                "Input numbers must not be null",
                "Input numbers must not be empty");
    }
}
