package pl.lotto.domain.numbersgenerator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;
import pl.lotto.infrastructure.client.RandomNumberGeneratorClient;

@Configuration
@Profile("test")
public class RandomNumberGeneratorConfiguration {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
    @Bean
    public RandomNumbersGeneratorFacade createModuleForTests(RestTemplate restTemplate, RandomNumberRepository randomNumberRepository) {
        RandomNumberGeneratorClient randomNumberGeneratorClient = new RandomNumberGeneratorClient(restTemplate);
        return new RandomNumbersGeneratorFacade(randomNumberGeneratorClient, randomNumberRepository);
    }
}
