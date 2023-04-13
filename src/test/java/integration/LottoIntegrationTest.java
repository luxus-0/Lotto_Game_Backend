package integration;

import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pl.lotto.domain.numbersgenerator.WinningNumbersFacade;
import pl.lotto.domain.numbersgenerator.WinningNumbersNotFoundException;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;

import java.time.Duration;
import java.time.LocalDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Log4j2
public class LottoIntegrationTest extends BaseIntegrationTest {

    @Autowired
    WinningNumbersFacade winningNumbersFacade;
    @Autowired
    RestTemplate restTemplate;

    @Test
    public void should_user_win_and_generate_winners() {
        wireMockServer.stubFor(get("random.org/integers/?num=6&min=1&max=99&format=plain&col=1&base=10")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                [1 2 3 4 5 6 23 56 78 89 90 43 65 87 40 11]
                                """.trim())));

        LocalDateTime drawDate = LocalDateTime.of(2022, 11, 19, 12, 0, 0);
        await()
                .atMost(Duration.ofSeconds(10))
                .pollInterval(Duration.ofSeconds(1))
                .until(() -> {
                    try {
                        return !winningNumbersFacade.retrieveWinningNumbersByDate(drawDate).winningNumbers().isEmpty();

                    } catch (WinningNumbersNotFoundException e) {
                        return false;
                    }
                });
    }

    @Test
    public void should_return_generate_six_random_numbers_with_correct_range() {
        StringValuePattern countPattern = equalTo("6");
        StringValuePattern rangeFromPattern = equalTo("1");
        StringValuePattern rangeToPattern = equalTo("99");

        wireMockServer.stubFor(get(urlMatching("/integers.*"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("1\n4\n6\n8\n10\n12\n"))
                .withQueryParam("num", countPattern)
                .withQueryParam("min", rangeFromPattern)
                .withQueryParam("max", rangeToPattern));

        ResponseEntity<String> response = restTemplate.getForEntity("https://random.org/integers/?num=6&min=1&max=99&format=plain&col=1&base=10", String.class);

        assertThat(response.getBody()).isNotEmpty();
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void ShouldThrowingExceptionWhenUrlIsIncorrect() {
        wireMockServer.stubFor(get(urlEqualTo("/integers.*"))
                .willReturn(aResponse()
                        .withHeader("Media-Type", "application/json")
                        .withBody("")
                        .withStatus(500)));

        assertThrows(IllegalArgumentException.class,
                () -> restTemplate.exchange("/integers?num=6&min=1&max=99", HttpMethod.GET, null, WinningNumbersDto.class));

    }

    @Test
    public void ShouldThrowingExceptionWhenUrlIsEmpty() {
        wireMockServer.stubFor(get(urlEqualTo(""))
                .willReturn(aResponse()
                        .withBody("")
                        .withStatus(500)));

        assertThrows(IllegalArgumentException.class,
                () -> restTemplate.exchange("", HttpMethod.GET, null, WinningNumbersDto.class));
    }
}
