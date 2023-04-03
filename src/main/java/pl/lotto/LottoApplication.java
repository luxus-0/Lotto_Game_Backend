package pl.lotto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.lotto.domain.numbersgenerator.WinningNumbersFacadeConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({WinningNumbersFacadeConfigurationProperties.class})
public class LottoApplication {
    public static void main(String[] args) {
        SpringApplication.run(LottoApplication.class, args);
    }
}