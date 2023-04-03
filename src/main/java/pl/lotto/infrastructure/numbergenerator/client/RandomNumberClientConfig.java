package pl.lotto.infrastructure.numbergenerator.client;

import org.springframework.beans.factory.annotation.Value;
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
    RestTemplate restTemplate(@Value("${numbers.genenerator.client.connectionTimeout}") long connectionTimeOut,
                              @Value("${numbers.genenerator.client.readTimeOut}") long readTimeOut,
                              ResponseErrorHandlerClient responseErrorHandlerClient) {
        return new RestTemplateBuilder()
                .errorHandler(responseErrorHandlerClient)
                .setConnectTimeout(Duration.ofMillis(connectionTimeOut))
                .setReadTimeout(Duration.ofMillis(readTimeOut))
                .build();
    }

    @Bean
    RandomNumberGeneratorClient numberGeneratorClient(RestTemplate restTemplate,
                                                      @Value("${numbers.generator.client.url}") String url) {
        return new RandomNumberGeneratorClient(restTemplate, url);
    }

}
