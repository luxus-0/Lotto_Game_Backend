package pl.lotto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.lotto.domain.numberreceiver.dto.InputNumbersRequestDto;
import pl.lotto.domain.numbersgenerator.WinningNumbersConfigurationProperties;
import pl.lotto.infrastructure.numbergenerator.client.TimeConnectionClient;
import pl.lotto.infrastructure.security.token.JwtConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({WinningNumbersConfigurationProperties.class, TimeConnectionClient.class, JwtConfigurationProperties.class})
@EnableScheduling
@EnableMongoRepositories
public class LottoApplication {
    public static void main(String[] args) {
        SpringApplication.run(LottoApplication.class, args);
    }
}