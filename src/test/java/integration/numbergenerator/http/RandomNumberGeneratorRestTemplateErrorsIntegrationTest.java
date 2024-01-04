package integration.numbergenerator.http;

import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.web.server.ResponseStatusException;
import pl.lotto.domain.numbersgenerator.RandomNumbersGenerator;
import pl.lotto.infrastructure.numbergenerator.client.TimeConnectionClient;

import java.time.Duration;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.apache.catalina.util.XMLWriter.NO_CONTENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.awaitility.Awaitility.await;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class RandomNumberGeneratorRestTemplateErrorsIntegrationTest {

    @RegisterExtension
    public static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    RandomNumbersGenerator randomNumbersGenerator = new RandomNumberGeneratorRestTemplateIntegrationTestConfig()
            .randomNumbersGeneratorClient(TimeConnectionClient.builder()
                    .readTimeOut(1000)
                    .connectionTimeOut(1000)
                    .build());

    @Test
    public void should_throw_exception_404_not_found_when_client_has_incorrect_out_of_band_and_wait_20_seconds() {
        //given
        wireMockServer.stubFor(get("random.org/integers/?num=6&min=99&max=1&format=plain&col=2&base=10")
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody("""
                                {
                                    "randomNumbers" : []
                                }
                                """.trim())
                        .withStatus(NOT_FOUND.value())));


        // when && then
        await()
                .atMost(Duration.ofSeconds(20))
                .pollInterval(Duration.ofSeconds(1))
                .until(() -> {
                    {
                        assertThatThrownBy(() ->
                                randomNumbersGenerator.generateRandomNumbers(1, 1, 100))
                                .isInstanceOf(ResponseStatusException.class)
                                .hasMessage(NOT_FOUND.toString());
                    }
                    return true;
                });
    }

    @Test
    @DisplayName("should throw exception 204 no content when client has no inputNumbers")
    public void should_throw_exception_204_no_content_when_client_has_no_numbers() {
        //given
        wireMockServer.stubFor(get("https://random.org/integers/?num=0&min=1&max=99&format=plain&col=2&base=10")
                .willReturn(aResponse()
                        .withStatus(NO_CONTENT)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody("""
                                [1 2 3 4 5 6 7 8 9 10]
                                """)));

        //when
        Throwable throwable = catchThrowable(() -> randomNumbersGenerator.generateRandomNumbers(0, 1, 99));

        //then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo("204 NO_CONTENT");
    }

    @Test
    @DisplayName("should throw exception 401 unauthorized when client is unauthorized response")
    public void should_throw_exception_401_unauthorized_when_client_is_unauthorized_response() {
        //given
        wireMockServer.stubFor(get("https://random.org/integers/?num=0&min=0&max=0&format=plain&col=2&base=10")
                .willReturn(aResponse()
                        .withStatus(UNAUTHORIZED.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)));

        //when
        Throwable throwable = catchThrowable(() -> randomNumbersGenerator.generateRandomNumbers(0, 0, 0));

        //then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo("401 UNAUTHORIZED");
    }

    @Test
    @DisplayName("should throw exception 404 not found when client fault connection reset by peer")
    public void should_throw_exception_404_not_found_when_fault_connection_reset_by_peer() {
        //given
        wireMockServer.stubFor(get("https://random.org/integers/?num=12&min=99&max=1&format=plain&col=2&base=10")
                .willReturn(aResponse()
                        .withStatus(NOT_FOUND.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withFault(Fault.CONNECTION_RESET_BY_PEER)));

        //when
        Throwable throwable = catchThrowable(() -> randomNumbersGenerator.generateRandomNumbers(12, 99, 1));

        //then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo("404 NOT_FOUND");
    }

    @Test
    @DisplayName("should throw exception 404 not found when client has out of bound inputNumbers")
    void should_throw_exception_404_not_found_when_client_has_out_of_bound_numbers() {
        // given
        wireMockServer.stubFor(get("https://random.org/integers/?num=25&min=1&max=102&format=plain&col=2&base=10")
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withStatus(NOT_FOUND.value()))
        );

        // when
        Throwable throwable = catchThrowable(() -> randomNumbersGenerator.generateRandomNumbers(6, 1, 102));

        // then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo("404 NOT_FOUND");
    }

    @Test
    @DisplayName("should throw exception 500 interval sever error when fault empty response")
    public void should_throw_exception_500_interval_sever_error_when_fault_empty_response() {
        //given
        wireMockServer.stubFor(get("https://random.org/integers/?num=15&min=99&max=30&format=plain&col=2&base=10")
                .willReturn(aResponse()
                        .withStatus(INTERNAL_SERVER_ERROR.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withFault(Fault.EMPTY_RESPONSE)));

        //when
        Throwable throwable = catchThrowable(() -> randomNumbersGenerator.generateRandomNumbers(15, 1, 99));

        //then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR");
    }

    @Test
    @DisplayName("should throw exception 500 interval sever error when response delay is 1500ms and read time out is 1000ms")
    public void should_throw_exception_500_interval_sever_error_when_response_delay_is_1500ms_and_read_time_out_is_1000ms() {
        //given
        wireMockServer.stubFor(get("https://random.org/integers/?num=12&min=1&max=99&format=plain&col=2&base=10")
                .willReturn(aResponse()
                        .withStatus(INTERNAL_SERVER_ERROR.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody("""
                                [1 2 3 4 5 6 23 56 43 11]
                                """.trim()
                        )
                        .withFixedDelay(1500)));

        //when
        Throwable throwable = catchThrowable(() -> randomNumbersGenerator.generateRandomNumbers(12, 1, 99));

        //then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR");
    }

    @Test
    @DisplayName("should throw exception 500 interval sever error when client has malformed response chunk")
    void should_throw_exception_500_interval_sever_error_when_fault_malformed_response_chunk() {
        // given
        wireMockServer.stubFor(get("https://random.org/integers/?num=25&min=1&max=99&format=plain&col=2&base=10")
                .willReturn(aResponse()
                        .withStatus(OK.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withFault(Fault.MALFORMED_RESPONSE_CHUNK)));
        // when
        Throwable throwable = catchThrowable(() -> randomNumbersGenerator.generateRandomNumbers(25, 1, 99));

        // then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR");
    }

    @Test
    @DisplayName("should throw exception 500 interval sever error when client has fault random data then close")
    void should_throw_exception_500_interval_sever_error_when_fault_random_data_then_close() {
        // given
        wireMockServer.stubFor(get("https://random.org/integers/?num=25&min=1&max=99&format=plain&col=2&base=10")
                .willReturn(aResponse()
                        .withStatus(OK.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withFault(Fault.RANDOM_DATA_THEN_CLOSE)));

        // when
        Throwable throwable = catchThrowable(() -> randomNumbersGenerator.generateRandomNumbers(25, 1, 99));

        // then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR");
    }
}