package integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.lotto.domain.numbersgenerator.WinningNumbersGeneratorFacade;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;

public class LottoIT extends BaseIntegrationTest {

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
        WinningNumbersDto winningNumbersDto = winningNumbersGeneratorFacade.generateWinningNumbers();
        System.out.println(winningNumbersDto);
    }
}
