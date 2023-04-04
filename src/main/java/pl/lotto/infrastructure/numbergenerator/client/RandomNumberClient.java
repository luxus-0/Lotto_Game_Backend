package pl.lotto.infrastructure.numbergenerator.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.lotto.domain.numbersgenerator.dto.RandomNumbersDto;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RandomNumberClient {
    @Value("${api.url}")
    private String url;
    @Value("${quantity.numbers}")
    private int quantityNumbers;

    @Value("${min.number}")
    private int minNumber;

    @Value("${max.number}")
    private int maxNumber;
    private final static String API_URL = "https://www.random.org/integers/?num=6&min=1&max=99&col=1&base=10&format=plain&unique=1";

    public RandomNumbersDto generateRandomNumbers() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.GET, entity, String.class);

        Set<Integer> randomNumbers = geRandomNumbers(Objects.requireNonNull(response.getBody()));

        return RandomNumbersDto.builder()
                .randomNumbers(randomNumbers)
                .build();
    }

    private Set<Integer> geRandomNumbers(String responseBody) {
        return Arrays.stream(responseBody.split("\n"))
                .map(Integer::valueOf)
                .collect(Collectors.toSet());
    }
}
