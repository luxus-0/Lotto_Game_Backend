package integration.token;

import integration.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.lotto.domain.login.User;
import pl.lotto.infrastructure.security.token.dto.TokenResponseDto;

import java.util.regex.Pattern;

import static integration.token.TokenIntegrationTestConstants.TOKEN_REGEX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
public class TokenIntegrationTest extends BaseIntegrationTest {

    @Test
    @DisplayName("login username and password and JWT not exist in database then return status 401 unauthorized")
    public void should_return_failed_login_when_body_username_and_password_has_status_400_unauthorized() throws Exception {
        // given & when

        ResultActions failedLoginRequest = mockMvc.perform(post("/token")
                .content("""
                        {
                        "username": "someUsers",
                        "password": "somePasswords"
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

    @Test
    @DisplayName("login username and password and JWT exist in database then return status 200 OK")
    void should_return_success_login_with_token_when_body_username_and_password_has_status_200_ok() throws Exception {
        //given
        loginRepository.save(User.builder()
                .username("someUsers")
                .password(new BCryptPasswordEncoder().encode("somePasswords"))
                .build());

        //when
        ResultActions successLoginRequest = mockMvc.perform(post("/token")
                .content("""
                        {
                            "username": "someUsers",
                            "password": "somePasswords"
                        }
                        """.trim())
                .contentType(APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION + " Bearer", properties.secret()));

        // then
        MvcResult mvcResult = successLoginRequest.andExpect(status().isOk()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        TokenResponseDto jwtResponse = objectMapper.readValue(json, TokenResponseDto.class);
        String token = jwtResponse.token();
        assertAll(
                () -> assertThat(jwtResponse.username()).isEqualTo("someUsers"),
                () -> assertThat(token).matches(Pattern.compile(TOKEN_REGEX))
        );
    }

    @Test
    @DisplayName("login username and password and JWT not exist in database then return status 401 unauthorized")
    void should_return_failed_login_when_body_username_and_password_has_status_401_unauthorized() throws Exception {
        //given
        loginRepository.save(User.builder()
                .username("user3")
                .password(new BCryptPasswordEncoder().encode("password3"))
                .build());

        //when
        ResultActions failedLoginRequest = mockMvc.perform(post("/token")
                .content("""
                        {
                            "username": "user",
                            "password": "password"
                        }
                        """.trim())
                .contentType(APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION + " Bearer", properties.secret()));

        // then
        MvcResult mvcResult = failedLoginRequest.andExpect(status().isUnauthorized()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        TokenResponseDto jwtResponse = objectMapper.readValue(json, TokenResponseDto.class);
        String token = jwtResponse.token();
        assertAll(
                () -> assertThat(jwtResponse.username()).isEqualTo(null),
                () -> assertThat(token).isEqualTo(null));
    }

    @Test
    @DisplayName("login username and password is empty with JWT than return status 403 forbidden")
    void should_return_failed_login_when_body_username_and_password_is_empty() throws Exception {
        //given
        loginRepository.save(User.builder()
                .username("")
                .password(new BCryptPasswordEncoder().encode(""))
                .build());

        //when
        ResultActions failedLoginRequest = mockMvc.perform(post("/token")
                .content("""
                        {
                            "username": "",
                            "password": ""
                        }
                        """.trim())
                .contentType(APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION + " Bearer", properties.secret()));

        // then
        MvcResult mvcResult = failedLoginRequest.andExpect(status().isForbidden()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();

         assertThrows(Exception.class, () ->  {
             TokenResponseDto result = objectMapper.readValue(json, TokenResponseDto.class);
             assertThat(result.username()).isEqualTo(null);
             assertThat(result.token()).isEqualTo(null);
         });
    }
}
