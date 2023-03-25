package pl.lotto.infrastructure.numbergenerator.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import pl.lotto.domain.numbersgenerator.dto.RandomNumberDto;

import java.util.Collections;

@RequiredArgsConstructor
public class NumberGeneratorClient {
    @Value("${random.numbers.api}")
    private String RANDOM_NUMBERS_API;
    @Value("${range.from.number}")
    private int RANGE_FROM_NUMBER;
    @Value("${range.to.number}")
    private int RANGE_TO_NUMBER;
    @Value("${quantity.numbers}")
    private int QUANTITY_NUMBERS;
    private final RestTemplate restTemplate;
    public ResponseEntity<RandomNumberDto> generateSixRandomNumbers(){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = getRandomNumbersUrl();

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                RandomNumberDto.class
                );
    }

    private String getRandomNumbersUrl() {
        return RANDOM_NUMBERS_API +
                "?num=" + QUANTITY_NUMBERS +
                "&min=" + RANGE_FROM_NUMBER +
                "&max=" + RANGE_TO_NUMBER +
                "&format=plain" +
                "&rnd=new";
    }
}
