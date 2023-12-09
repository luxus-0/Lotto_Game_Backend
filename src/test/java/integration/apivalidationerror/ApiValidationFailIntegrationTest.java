package integration.apivalidationerror;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.tomakehurst.wiremock.client.WireMock;
import integration.BaseIntegrationTest;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.lotto.domain.login.dto.RegistrationResultDto;
import pl.lotto.domain.numbersgenerator.dto.WinningTicketResponseDto;
import pl.lotto.infrastructure.numbergenerator.scheduler.WinningNumbersScheduler;
import pl.lotto.infrastructure.security.token.dto.TokenResponseDto;

import java.util.regex.Pattern;

import static integration.apivalidationerror.ApiValidationFailIntegrationTestConstant.REGEX_TOKEN;
import static integration.apivalidationerror.ApiValidationFailIntegrationTestConstant.bodyWithNoWinningNumbersJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
public class ApiValidationFailIntegrationTest extends BaseIntegrationTest {

    @Autowired
    WinningNumbersScheduler winningNumbersScheduler;



    @Test
    public void should_return_400_bad_request_and_validation_message_when_request_has_empty_input_numbers() {
        //given && when
        try {
            MvcResult perform = mockMvc.perform(post("/inputNumbers")
                            .content("""
                                    {
                                    "inputNumbers" : []
                                    }
                                    """.trim()
                            ).contentType(APPLICATION_JSON)
                    ).andExpect(status().isBadRequest())
                    .andReturn();

            //then
            String json = perform.getResponse().getContentAsString();
            ApiValidationErrorDto result = objectMapper.readValue(json, ApiValidationErrorDto.class);
            assertThat(result.messages()).containsExactlyInAnyOrder(
                    "inputNumbers must not be empty",
                    "inputNumbers must not be null");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Test
    public void should_return_400_bad_request_and_validation_message_when_does_not_have_input_numbers() {
        //given && when
        try {
            MvcResult perform = mockMvc.perform(post("/inputNumbers")
                            .content("""
                                    {
                                                                       
                                     }
                                     """.trim()
                            ).contentType(APPLICATION_JSON)
                    ).andExpect(status().isBadRequest())
                    .andReturn();

            //then
            String json = perform.getResponse().getContentAsString();
            ApiValidationErrorDto result = objectMapper.readValue(json, ApiValidationErrorDto.class);
            assertThat(result.messages()).containsExactlyInAnyOrder(
                    "inputNumbers must not be empty",
                    "inputNumbers must not be null");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Test
    public void user_want_to_see_no_winning_numbers_but_have_to_be_logged_in_and_external_server_should_have_some_winning_numbers() throws Exception {
        // step 1: there are no winning numbers in external HTTP server
        // given && when && then
        wireMockServer.stubFor(WireMock.get("/winning_numbers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyWithNoWinningNumbersJson())));
        // step 2: scheduler ran 1st time and made GET to external server and system added 0 winning numbers to database
        // given && when
        // then

        //step 3: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned FORBIDDEN(403)
        //given && when
        ResultActions failedLoginRequest = mockMvc.perform(post("/token")
                .contentType("application/json")
                .content("""
                        {
                             "username": "someUser",
                            "password": "somePassword"
                        }
                        """.trim()));

        // then
        failedLoginRequest
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "message": "Bad Credentials",
                          "httpStatus": "UNAUTHORIZED"
                        }
                        """.trim()));


        //step 4: user made GET /winning_numbers with no jwt token and system returned Unauthorized(401)
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
        failedLogin.andExpect(status().isUnauthorized());


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
                () -> AssertionsForClassTypes.assertThat(registrationResultDto.username()).isEqualTo("someUser"),
                () -> AssertionsForClassTypes.assertThat(registrationResultDto.created()).isTrue(),
                () -> AssertionsForClassTypes.assertThat(registrationResultDto.id()).isNotNull()
        );

        //step 6: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned OK(200) and jwttoken=AAAA.BBBB.CCC
        // given & when
        ResultActions successLoginRequest = mockMvc.perform(post("/token")
                .content("""
                        {
                        "username": "someUser",
                        "password": "somePassword"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        MvcResult mvcResult = successLoginRequest.andExpect(status().isOk()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        TokenResponseDto jwtResponse = objectMapper.readValue(json, TokenResponseDto.class);
        String token = jwtResponse.token();
        assertAll(
                () -> assertThat(jwtResponse.username()).isEqualTo("someUser"),
                () -> assertThat(token).matches(Pattern.compile(REGEX_TOKEN))
        );

        //step 7: user made GET /winning_numbers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with no winning numbers
        //given
        ResultActions getWinningNumbersToken = mockMvc.perform(get("/winning_numbers")
                .header("Authorization", "Bearer " + token)
                .contentType(APPLICATION_JSON));

        //then
        MvcResult mvcResult2 = getWinningNumbersToken.andExpect(status().isOk()).andReturn();
        String jsonWinningNumbers = mvcResult2.getResponse().getContentAsString();
        WinningTicketResponseDto winningTicketResponseDto = objectMapper.readValue(jsonWinningNumbers, new TypeReference<>() {
        });
        assertThat(winningTicketResponseDto.winningNumbers()).isEmpty();

    }
}
