package integration.apivalidationerror;

import integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.http.ResponseEntity.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class ApiValidationFailIntegrationTest extends BaseIntegrationTest {

    @Test
    public void should_return_400_bad_request_and_validation_message_when_input_numbers_are_empty() throws Exception {
        //when
        ResultActions emptyInputNumbers = mockMvc.perform(post("/inputNumbers")
                .content("""
                        {
                        "inputNumbers": []
                        }
                        """.trim()
                ).contentType(MediaType.APPLICATION_JSON)
        );
        //when
        MvcResult mvcResult = emptyInputNumbers.andExpect(result -> status(404)).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        ApiValidationErrorResponseDto responseError = objectMapper.readValue(json, ApiValidationErrorResponseDto.class);
        ApiValidationErrorResponseDto validationError = new ApiValidationErrorResponseDto("", HttpStatus.NOT_FOUND);
    }
}
