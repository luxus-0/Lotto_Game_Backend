package integration.numbergenerator.http;

import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import pl.lotto.domain.numbersgenerator.RandomNumbersGenerable;
import pl.lotto.infrastructure.numbergenerator.client.TimeConnectionClient;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class RandomNumberGeneratorRestTemplateErrorsIntegrationTest {

    public static final String CONTENT_TYPE_HEADER_KEY = "Content-Type";
    public static final String APPLICATION_JSON_CONTENT_TYPE_VALUE = "application/json";

    @RegisterExtension
    public static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    RandomNumbersGenerable randomNumbersGenerable = new RandomNumberGeneratorRestTemplateIntegrationTestConfig()
            .randomNumbersGeneratorClient(TimeConnectionClient.builder()
                    .readTimeOut(1000)
                    .connectionTimeOut(1000)
                    .build());

    @Test
    public void should_return_null_numbers_when_fault_connection_reset_by_peer() {
        //given
        wireMockServer.stubFor(get("https://random.org/integers/?num=6&min=1&max=99&format=plain&col=2&base=10")
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(CONTENT_TYPE_HEADER_KEY, APPLICATION_JSON_CONTENT_TYPE_VALUE)
                        .withFault(Fault.CONNECTION_RESET_BY_PEER)));

        //when
        Throwable throwable = catchThrowable(() -> randomNumbersGenerable.generateRandomNumbers(0, 1, 99));

        //then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR");
    }

    @Test
    public void should_return_null_numbers_when_fault_empty_response(){
        //given
        wireMockServer.stubFor(get("https://random.org/integers/?num=6&min=1&max=99&format=plain&col=2&base=10")
                .willReturn(aResponse()
                        .withStatus(HttpStatus.NOT_FOUND.value())
                        .withHeader(CONTENT_TYPE_HEADER_KEY, APPLICATION_JSON_CONTENT_TYPE_VALUE)
                        .withFault(Fault.EMPTY_RESPONSE)));

        //when
        Throwable throwable = catchThrowable(() ->  randomNumbersGenerable.generateRandomNumbers(9, 1, 101));

        //then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo("404 NOT_FOUND");
    }

    @Test
    public void should_return_null_numbers_when_status_is_204_no_content(){
        //given
        wireMockServer.stubFor(get("https://random.org/integers/?num=6&min=1&max=99&format=plain&col=2&base=10")
                .willReturn(aResponse()
                        .withStatus(HttpStatus.NO_CONTENT.value())
                        .withHeader(CONTENT_TYPE_HEADER_KEY, APPLICATION_JSON_CONTENT_TYPE_VALUE)
                        .withBody("""
                                [1 2 3 4 5 6 7]
                                """)));

        //when
        Throwable throwable = catchThrowable(() ->  randomNumbersGenerable.generateRandomNumbers(0, 0, 0));

        //then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo("204 NO_CONTENT");
    }
}
