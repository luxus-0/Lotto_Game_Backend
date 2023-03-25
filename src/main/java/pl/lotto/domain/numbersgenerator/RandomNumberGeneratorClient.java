package pl.lotto.domain.numbersgenerator;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.lotto.domain.numbersgenerator.dto.RandomNumberDto;

import java.util.Collections;

@RequiredArgsConstructor
@Service
class RandomNumberGeneratorClient {
    @Value("${random.numbers.api}")
    private String RANDOM_NUMBERS_API;
    private final RestTemplate restTemplate;
    private final static int LOWER_BAND = 1;
    private final static int UPPER_BAND = 99;
    private final static int QUANTITY_NUMBERS = 6;
    public ResponseEntity<RandomNumberDto> generateSixRandomNumbers(){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = getRandomNumbersApi();

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                RandomNumberDto.class
                );
    }

    private String getRandomNumbersApi() {
        return RANDOM_NUMBERS_API +
                "?num=" + QUANTITY_NUMBERS +
                "&min=" + LOWER_BAND +
                "&max=" + UPPER_BAND +
                "&format=plain" +
                "&rnd=new";
    }
}
