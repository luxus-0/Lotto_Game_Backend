package pl.lotto.infrastructure.numbergenerator.client;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import pl.lotto.domain.numbersgenerator.WinningNumbersFacadeConfigurationProperties;

import java.time.Duration;

@Configuration
public class RandomNumberClientConfig {

    @Bean
    public ResponseErrorHandlerClient responseErrorHandlerClient() {
        return new ResponseErrorHandlerClient();
    }

    @Bean
    RestTemplate restTemplate(WinningNumbersFacadeConfigurationProperties properties, ResponseErrorHandlerClient responseErrorHandlerClient) {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(properties.connectionTimeOut()))
                .setReadTimeout(Duration.ofMillis(properties.readTimeOut()))
                .errorHandler(responseErrorHandlerClient)
                .build();
    }
}
