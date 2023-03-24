package pl.lotto.domain.numbersgenerator.exception;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import pl.lotto.domain.numbersgenerator.dto.RandomNumbersDto;

import java.util.Collections;
import java.util.Set;

@AllArgsConstructor
public class RandomNumberGeneratorClient {
    @Value("${random.numbers.api}")
    private final String RANDOM_NUMBERS_API;
    private final RestTemplate restTemplate;
    private final static int LOWER_BAND = 1;
    private final static int UPPER_BAND = 99;
    private final static int SIZE_NUMBERS = 6;
    public ResponseEntity<RandomNumbersDto> generateSixRandomNumbers(){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = getRandomNumbersApi();

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                RandomNumbersDto.class
                );
    }

    private String getRandomNumbersApi() {
        return RANDOM_NUMBERS_API +
                "?num=" + SIZE_NUMBERS +
                "&min=" + LOWER_BAND +
                "&max=" + UPPER_BAND +
                "&format=plain" +
                "&rnd=new";
    }
}
