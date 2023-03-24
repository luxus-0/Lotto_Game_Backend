package pl.lotto.domain.numbersgenerator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
class RandomNumbersGeneratorConfiguration {

    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
