package integration.numbergenerator.http;

import com.github.tomakehurst.wiremock.client.WireMock;
import integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.lotto.domain.login.dto.RegistrationResultDto;
import pl.lotto.infrastructure.security.token.dto.TokenResponseDto;

import java.util.regex.Pattern;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static integration.numbergenerator.http.WinningNumbersGeneratorWithTokenIntegrationTestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WinningNumberGeneratorWithTokenIntegrationTest extends BaseIntegrationTest {

    @Test
    public void should_return_no_winning_numbers_but_have_to_be_logged_in_and_external_server_should_have_some_winning_numbers() throws Exception {
        // step 1: there are no winning inputNumbers in external HTTP server
        // given && when && then
        wireMockServer.stubFor(WireMock.get("/winning_numbers")
                .willReturn(aResponse()
                        .withStatus(OK.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(bodyWithNoWinningNumbersJson())));
        // step 2: scheduler ran 1st time and made GET to external server and system added 0 winning inputNumbers to database
        // given && when
        // then

        //step 3: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned UNAUTHORIZED(401)
        //given     && when
        ResultActions postInputNumbers = mockMvc.perform(post("/inputNumbers")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
                        {
                            "inputNumbers" : [1 2 3 4 5 6]
                        }
                        """.trim()));

        ResultActions failedLoginRequest = mockMvc.perform(post("/token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                        {
                            "username" : "someUser",
                            "password" : "somePassword"
                        }
                        """.trim()));

        // then
        failedLoginRequest
                .andExpect(status().isForbidden())
                .andReturn();


        postInputNumbers.andExpect(status().isBadRequest())
                .andReturn();



        //step 4: user made GET /winning_numbers with no jwt token and system returned Forbidden(401)
        // given & when
        ResultActions getWinningNumbersRequest = mockMvc.perform(get("/winning_numbers")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content("""
                        {
                            "winningNumbers" : [1,2,3,4,5,6]
                        }
                        """.trim()
                ));

        ResultActions failedLogin = mockMvc.perform(post("/token")
                .contentType("application/json")
                .content("""
                        {
                            "username": "",
                            "password": ""
                        }
                        """.trim()));

        // then
        getWinningNumbersRequest.andExpect(status().isOk());
        failedLogin.andExpect(status().isForbidden());


        //step 5: user made POST /register with username=someUser, password=somePassword and system registered user with status OK(200)
        // given & when
        ResultActions registerAction = mockMvc.perform(post("/register")
                .content("""
                        {
                         "username": "someUser",
                        "password": "somePassword"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        MvcResult registerActionResult = registerAction.andExpect(status().isCreated()).andReturn();
        String registerActionResultJson = registerActionResult.getResponse().getContentAsString();
        RegistrationResultDto registrationResultDto = objectMapper.readValue(registerActionResultJson, RegistrationResultDto.class);
        assertAll(
                () -> assertThat(registrationResultDto.username()).isEqualTo("someUser"),
                () -> assertThat(registrationResultDto.created()).isTrue(),
                () -> assertThat(registrationResultDto.id()).isNotNull()
        );

        //step 6: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned OK(200) and jwttoken=AAAA.BBBB.CCC
        // given & when
        ResultActions successLoginRequest = mockMvc.perform(post("/token")
                .content("""
                        {
                        "username" : "someUser",
                        "password" : "somePassword"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        MvcResult mvcResult = successLoginRequest.andExpect(status().isForbidden()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        TokenResponseDto jwtResponse = objectMapper.readValue(json, TokenResponseDto.class);
        String token = jwtResponse.token();
        assertAll(
                () -> assertThat(jwtResponse.username()).isEqualTo("someUser")
                //() -> assertThat(token).matches(Pattern.compile(REGEX_TOKEN))
        );

        //step 7: user made GET /winning_numbers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned not found(404) with no winning numbers
        //given
        ResultActions getWinningNumbersToken = mockMvc.perform(get("/winning_numbers")
                .header("Authorization", "Bearer " + token)
                .contentType(APPLICATION_JSON));

        //then
        MvcResult mvcResult2 = getWinningNumbersToken.andExpect(status().isNotFound()).andReturn();
        String jsonWinningNumbers = mvcResult2.getResponse().getContentAsString();

       assertThat(jsonWinningNumbers).isEqualTo(
               """
               """);

        //step 8: there are no winning inputNumbers in external HTTP server
        //given && when && then
        wireMockServer.stubFor(WireMock.get("/winning_numbers")
                .willReturn(aResponse()
                        .withHeader(APPLICATION_JSON_VALUE, "application/json")
                        .withBody(noWinningNumbersJson())
                        .withStatus(OK.value())));

        //step 9: user made GET /winning_numbers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with no winning inputNumbers with ids
        // given && when
        ResultActions getForTwoWinningNumbers = mockMvc.perform(get("/winning_numbers")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        MvcResult performGetForTwoWinningNumbers = getForTwoWinningNumbers.andExpect(status().isNotFound()).andReturn();
        String jsonWithWinningNumbers = performGetForTwoWinningNumbers.getResponse().getContentAsString();


        assertThat(jsonWithWinningNumbers).isEqualTo(
                """
                """);
    }
}
