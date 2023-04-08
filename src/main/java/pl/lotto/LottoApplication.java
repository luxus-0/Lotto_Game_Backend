package pl.lotto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.lotto.domain.numbersgenerator.WinningNumbersFacadeConfigurationProperties;
import pl.lotto.infrastructure.numbergenerator.client.RandomNumberParametersUrl;

@SpringBootApplication
@EnableConfigurationProperties({WinningNumbersFacadeConfigurationProperties.class, RandomNumberParametersUrl.class})
public class LottoApplication {
    public static void main(String[] args) {
        SpringApplication.run(LottoApplication.class, args);
    }
}