package integration.login;

import integration.BaseIntegrationTest;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.lotto.infrastructure.security.token.dto.TokenResponseDto;

import static integration.token.TokenIntegrationTestConstants.TOKEN_REGEX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
@WithMockUser
public class LoginIntegrationTest extends BaseIntegrationTest {

    @Test
    public void should_return_correct_login_credentials_with_token() throws Exception{
            mockMvc.perform(post("/register")
                            .content("""
                                    {
                                        "username": "someUser",
                                        "password": "somePassword"
                                    }
                                    """.trim())
                            .contentType(APPLICATION_JSON_VALUE)
                            .header(AUTHORIZATION + " Bearer", properties.secret()))
                    .andReturn();

            ResultActions login = mockMvc.perform(post("/login")
                    .content("""
                            {
                                "username": "someUser",
                                "password": "somePassword"
                            }
                            """.trim())
                    .contentType(APPLICATION_JSON_VALUE)
            );

            MvcResult loginResult = login.andExpect(status().isCreated()).andReturn();
            String loginJson = loginResult.getResponse().getContentAsString();
            TokenResponseDto result = objectMapper.readValue(loginJson, TokenResponseDto.class);

            assertAll(
                    () -> assertThat(result.username()).isEqualTo("someUser"),
                    () -> assertThat(result.token()).containsPattern(TOKEN_REGEX));
    }

    @Test
    public void should_throw_bad_request_login_when_username_and_password_is_empty() throws Exception {
        mockMvc.perform(post("/register")
                        .content("""
                                    {
                                        "username": "",
                                        "password": ""
                                    }
                                    """.trim())
                        .contentType(APPLICATION_JSON_VALUE)
                        .header(AUTHORIZATION + " Bearer", properties.secret()))
                .andReturn();

        ResultActions login = mockMvc.perform(post("/login")
                .content("""
                            {
                                "username": "",
                                "password": ""
                            }
                            """.trim())
                .contentType(APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION + " Bearer", properties.secret()));

        MvcResult loginResult = login.andExpect(status().isForbidden()).andReturn();
        String loginJson = loginResult.getResponse().getContentAsString();

        assertThat(loginJson).isEqualTo("");
    }
}
