package pl.lotto.infrastructure.numbergenerator.client;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RandomNumberClientConfig {

    @Bean
    public ResponseErrorHandlerClient responseErrorHandlerClient() {
        return new ResponseErrorHandlerClient();
    }

    @Bean
    public RandomNumberClientTimeConnection randomNumberClientTimeConnection(){
        return new RandomNumberClientTimeConnection();
    }

    @Bean
    RestTemplate restTemplate(RandomNumberClientTimeConnection timeConnection, ResponseErrorHandlerClient responseErrorHandlerClient) {
        return new RestTemplateBuilder()
                .errorHandler(responseErrorHandlerClient)
                .setConnectTimeout(Duration.ofMillis(timeConnection.getConnectionTimeOut()))
                .setReadTimeout(Duration.ofMillis(timeConnection.getReadTimeOut()))
                .build();
    }

    @Bean
    RandomNumberClient numberGeneratorClient(RestTemplate restTemplate) {
        return new RandomNumberClient(restTemplate);
    }

}
