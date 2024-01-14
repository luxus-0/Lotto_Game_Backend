package integration.login;

import integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.lotto.domain.login.LoginAndRegisterFacade;
import pl.lotto.domain.login.LoginRepository;
import pl.lotto.domain.login.User;
import pl.lotto.domain.login.dto.UserDto;
import pl.lotto.infrastructure.security.token.dto.TokenResponseDto;

import java.util.Optional;

import static integration.token.TokenIntegrationTestConstants.TOKEN_REGEX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.lotto.domain.login.LoginAndRegisterFacade.USER_NOT_FOUND;

@WithMockUser
public class LoginIntegrationTest extends BaseIntegrationTest {
    @Mock
    private LoginRepository loginRepository;

    @InjectMocks
    private LoginAndRegisterFacade loginAndRegisterFacade;

    @Test
    public void should_return_correct_login_credentials() {
        User testUser = new User("12345", "testUsername", "testPassword");
        when(loginRepository.findByUsername("testUsername")).thenReturn(Optional.of(testUser));

        UserDto actualUser = loginAndRegisterFacade.findByUsername("testUsername");

        assertEquals("12345", actualUser.uuid());
        assertEquals("testUsername", actualUser.username());
        assertEquals("testPassword", actualUser.password());
    }

    @Test
    public void should_throw_bad_credentials_when_username_not_found() {
        when(loginRepository.findByUsername("testUsername"))
                .thenReturn(Optional.of(new User("12345", "testUsername", "testPassword")));

        when(loginRepository.findByUsername("errorUsername"))
                .thenThrow(new BadCredentialsException(USER_NOT_FOUND));

        assertThrows(BadCredentialsException.class, () -> loginAndRegisterFacade.findByUsername("errorUsername"));
    }

    @Test
    public void should_throw_illegal_argument_exception_when_username_is_null() {
        when(loginRepository.findByUsername(null))
                .thenReturn(Optional.of(new User(null, null, null)));

        when(loginRepository.findByUsername(null))
                .thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class, () -> loginAndRegisterFacade.findByUsername(null));
    }

    @Test
    public void should_return_correct_login_credentials_with_token() throws Exception {
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
}
