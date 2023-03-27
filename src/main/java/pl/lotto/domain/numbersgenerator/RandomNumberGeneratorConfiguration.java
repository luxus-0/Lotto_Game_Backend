package pl.lotto.domain.numbersgenerator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;
import pl.lotto.infrastructure.numbergenerator.client.NumberGeneratorClient;

@Configuration
@Profile("test")
public class RandomNumberGeneratorConfiguration {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
    @Bean
    public RandomNumberGeneratorFacade createModuleForTests(WinningNumbersRepository winningNumbersRepository) {
        NumberGeneratorClient numberGeneratorClient = new NumberGeneratorClient();
        return new RandomNumberGeneratorFacade(numberGeneratorClient, winningNumbersRepository);
    }
}
