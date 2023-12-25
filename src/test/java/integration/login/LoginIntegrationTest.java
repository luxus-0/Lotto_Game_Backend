package integration.login;

import integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.lotto.infrastructure.security.token.dto.TokenResponseDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
public class LoginIntegrationTest extends BaseIntegrationTest {

    @Test
    public void should_return_failed_login_when_body_username_and_password_return_bad_credentials_401() throws Exception {
        // given & when

        ResultActions failedLoginRequest = mockMvc.perform(post("/token")
                .content("""
                        {
                        "username": "someUser",
                        "password": "somePassword"
                        }
                        """.trim())
                .contentType(APPLICATION_JSON_VALUE)
        );

        failedLoginRequest
                .andExpect(status().is(401))
                .andExpect(content().json("""
                        {
                          "message": "Bad Credentials",
                          "httpStatus": "UNAUTHORIZED"
                        }
                        """.trim()));

        // then
        MvcResult mvcResult = failedLoginRequest.andDo(print()).andExpect(status().isUnauthorized()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        TokenResponseDto jwtResponse = objectMapper.readValue(json, TokenResponseDto.class);
        assertThat(jwtResponse).isNotNull();
    }
}
