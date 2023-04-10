package integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.lotto.domain.numbersgenerator.WinningNumbersGeneratorFacade;
import pl.lotto.domain.numbersgenerator.WinningNumbersNotFoundException;

import java.time.Duration;
import java.time.LocalDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static org.awaitility.Awaitility.await;

public class LottoIntegrationTest extends BaseIntegrationTest {

    @Autowired
    WinningNumbersGeneratorFacade winningNumbersGeneratorFacade;

    @Test
    public void should_user_win_and_generate_winners() {
        wireMockServer.stubFor(get("https://random.org/integers/?num=6&min=1&max=99&format=plain&col=1&base=10")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-type", "text/plain")
                        .withBody("""
                                [1 2 3 4 5 6 23 56 78 89 90 43 65 87 40 11]
                                """.trim()
                        )));

        LocalDateTime drawDate = LocalDateTime.of(2023, 2, 25, 12, 0, 0);
        await()
                .atMost(Duration.ofSeconds(20))
                .pollInterval(Duration.ofSeconds(1))
                .until(() -> {
                    boolean areWinningNumbers = !winningNumbersGeneratorFacade.retrieveWinningNumbersByDate(drawDate).winningNumbers().isEmpty();
                    if (areWinningNumbers) {
                        return true;
                    }
                    throw new WinningNumbersNotFoundException("Winning numbers not found");
                });
    }
}
