package integration.register;

import integration.BaseIntegrationTest;
import lombok.extern.log4j.Log4j2;
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

@Log4j2
@WithMockUser
public class RegisterIntegrationTest extends BaseIntegrationTest {

    @Test
    public void should_return_register_successful_created_with_status_201_when_body_is_username_and_password() throws Exception {
        //given && when
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
    public void should_return_register_successful_created_with_status_201_when_body_is_empty_username_and_password() throws Exception {
        //given && when
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

    @Test
    public void should_throw_exception_with_message_when_body_is_empty() {
        //given && when
        try {
            ResultActions register = mockMvc.perform(post("/register")
                    .content("""
                            {
                               
                            }
                            """.trim())
                    .contentType(APPLICATION_JSON_VALUE)
                    .header(MEDIA_TYPE, APPLICATION_JSON_VALUE));


            MvcResult mvcResult = register.andExpect(status().isBadRequest()).andReturn();
            String json = mvcResult.getResponse().getContentAsString();
            RegistrationResultDto result = objectMapper.readValue(json, RegistrationResultDto.class);

            //then
            assertAll(
                    () -> assertThat(result.username()).isEqualTo(null),
                    () -> assertThat(result.created()).isFalse(),
                    () -> assertThat(result.uuid()).isNullOrEmpty()
            );
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    @Test
    public void should_return_register_successful_when_body_username_and_password_are_numbers() {
        //given && when
        try {
            ResultActions registerResult = mockMvc.perform(post("/register")
                    .content("""
                            {
                               "username" : 1,
                               "password" : 2
                            }
                            """.trim())
                    .contentType(APPLICATION_JSON_VALUE)
                    .header(MEDIA_TYPE, APPLICATION_JSON_VALUE));


            MvcResult mvcResult = registerResult.andExpect(status().isCreated()).andReturn();
            String json = mvcResult.getResponse().getContentAsString();
            RegistrationResultDto result = objectMapper.readValue(json, RegistrationResultDto.class);

            //then
            assertAll(
                    () -> assertThat(result.username()).isEqualTo("1"),
                    () -> assertThat(result.created()).isTrue(),
                    () -> assertThat(result.uuid()).isNotNull()
            );

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
