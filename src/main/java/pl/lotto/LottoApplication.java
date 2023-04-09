package pl.lotto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.lotto.domain.numbersgenerator.WinningNumbersFacadeConfigurationProperties;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersGeneratorParamURLDto;
import pl.lotto.infrastructure.numbergenerator.client.TimeConnectionClientDto;

@SpringBootApplication
@EnableConfigurationProperties({WinningNumbersFacadeConfigurationProperties.class, WinningNumbersGeneratorParamURLDto.class, TimeConnectionClientDto.class})
@EnableScheduling
public class LottoApplication {
    public static void main(String[] args) {
        SpringApplication.run(LottoApplication.class, args);
    }
}