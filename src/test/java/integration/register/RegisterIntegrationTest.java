package integration.register;

import integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.lotto.domain.login.dto.RegistrationResultDto;

import static javax.xml.transform.OutputKeys.MEDIA_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
public class RegisterIntegrationTest extends BaseIntegrationTest {

    @Test
    public void should_return_register_successful_created_with_status_201_when_body_return_username_and_password() throws Exception {
        ResultActions register = mockMvc.perform(post("/register")
                .content("""
                        {
                            "username": "someUser",
                            "password": "somePassword"
                        }
                        """.trim())
                .contentType(APPLICATION_JSON_VALUE)
        );
        // then
        MvcResult registerResult = register.andExpect(status().isCreated()).andReturn();
        String registerJson = registerResult.getResponse().getContentAsString();
        RegistrationResultDto registrationResultDto = objectMapper.readValue(registerJson, RegistrationResultDto.class);
        assertAll(
                () -> assertThat(registrationResultDto.username()).isEqualTo("someUser"),
                () -> assertThat(registrationResultDto.created()).isTrue(),
                () -> assertThat(registrationResultDto.uuid()).isNotNull()
        );

    }

    @Test
    public void should_return_register_successful_created_with_status_201_when_body_return_empty_username_and_password() throws Exception {
        ResultActions registerAction = mockMvc.perform(post("/register")
                .content("""
                        {
                            "username": "",
                            "password": ""
                        }
                        """.trim())
                .contentType(APPLICATION_JSON_VALUE)
                .header(MEDIA_TYPE, APPLICATION_JSON_VALUE));

        // then
        MvcResult registerResult = registerAction.andExpect(status().isCreated()).andReturn();
        String registerJson = registerResult.getResponse().getContentAsString();
        RegistrationResultDto registrationResultDto = objectMapper.readValue(registerJson, RegistrationResultDto.class);
        assertAll(
                () -> assertThat(registrationResultDto.username()).isEqualTo(""),
                () -> assertThat(registrationResultDto.created()).isTrue(),
                () -> assertThat(registrationResultDto.uuid()).isNotNull()
        );

    }
}
