package integration.apivalidationerror;

import integration.BaseIntegrationTest;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
public class ApiValidationFailIntegrationTest extends BaseIntegrationTest {

    @Test
    public void should_return_400_bad_request_and_validation_message_when_request_has_empty_input_numbers() throws Exception {
        //given && when
        try {
            MvcResult perform = mockMvc.perform(post("/inputNumbers")
                            .content("""
                                    {
                                    "inputNumbers" : []
                                    }
                                    """.trim()
                            ).contentType(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isBadRequest())
                    .andReturn();

            //then
            String json = perform.getResponse().getContentAsString();
            ApiValidationErrorDto result = objectMapper.readValue(json, ApiValidationErrorDto.class);
            assertThat(result.messages()).containsExactlyInAnyOrder(
                    "inputNumbers must not be empty");
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    @Test
    public void should_return_400_bad_request_and_validation_message_when_does_not_have_input_numbers() throws Exception {
        //given && when
        try {
            MvcResult perform = mockMvc.perform(post("/inputNumbers")
                            .content("""
                                   {
                                   
                                    }
                                    """.trim()
                            ).contentType(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isBadRequest())
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
}
