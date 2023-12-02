package pl.lotto.infrastructure.numbergenerator.client;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import pl.lotto.domain.numbersgenerator.WinningNumbersConfigurationProperties;

import java.time.Duration;

@Configuration
public class RandomNumberGeneratorClientConfig {
    @Bean
    public ResponseErrorHandlerClient responseErrorHandlerClient() {
        return new ResponseErrorHandlerClient();
    }

    @Bean
    public RestTemplate restTemplate(TimeConnectionClient connection, ResponseErrorHandlerClient responseErrorHandlerClient) {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(connection.connectionTimeOut()))
                .setReadTimeout(Duration.ofMillis(connection.readTimeOut()))
                .errorHandler(responseErrorHandlerClient)
                .build();
    }

    @Bean
    public RandomNumberGeneratorClient randomNumbersGeneratorClient(RestTemplate restTemplate, WinningNumbersConfigurationProperties properties, RandomNumberGeneratorClientValidator validator) {
        return new RandomNumberGeneratorClient(restTemplate, properties, validator);
    }

    @Bean
    public RandomNumberGeneratorClientValidator numberGeneratorClientValidator(WinningNumbersConfigurationProperties properties){
        return new RandomNumberGeneratorClientValidator(properties);
    }
}
