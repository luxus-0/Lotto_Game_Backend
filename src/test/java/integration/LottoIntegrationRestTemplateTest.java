package integration;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pl.lotto.domain.numbersgenerator.dto.WinningTicketDto;

import java.util.Objects;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class LottoIntegrationRestTemplateTest extends BaseIntegrationTest {
    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void should_return__status_200_when_generate_six_random_numbers_with_correct_range() {
        //given
        StringValuePattern countPattern = equalTo("6");
        StringValuePattern rangeFromPattern = equalTo("1");
        StringValuePattern rangeToPattern = equalTo("99");

        wireMockServer.stubFor(WireMock.get(urlMatching("/integers.*"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("1\n4\n6\n8\n10\n12\n"))
                .withQueryParam("num", countPattern)
                .withQueryParam("min", rangeFromPattern)
                .withQueryParam("max", rangeToPattern));
        //when
        ResponseEntity<String> response = restTemplate.getForEntity("https://random.org/integers/?num=6&min=1&max=99&format=plain&col=1&base=10", String.class);
        //then
        assertThat(response.getBody()).isNotEmpty();
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void shouldThrowingExceptionWhenUrlIsIncorrect() {
        //given
        wireMockServer.stubFor(WireMock.get(urlEqualTo("/integers.*"))
                .willReturn(aResponse()
                        .withHeader("Media-Type", "application/json")
                        .withBody("")
                        .withStatus(500)));
        //when
        //then
        assertThrowsExactly(IllegalArgumentException.class,
                () -> Objects.requireNonNull(
                        restTemplate.exchange("/integers?num=6&min=1&max=99", HttpMethod.GET, null, WinningTicketDto.class)
                                .getBody()));
    }

    @Test
    public void ShouldThrowingExceptionWhenUrlIsEmpty() {
        //given
        wireMockServer.stubFor(WireMock.get(urlEqualTo("/integers.*"))
                .willReturn(aResponse()
                        .withHeader("Media-Type", "application/json")
                        .withBody("")
                        .withStatus(500)));
        //when
        //then
        assertThrows(IllegalArgumentException.class,
                () -> Objects.requireNonNull(restTemplate.exchange("", HttpMethod.GET, null, WinningTicketDto.class)
                        .getBody()));
    }
}
