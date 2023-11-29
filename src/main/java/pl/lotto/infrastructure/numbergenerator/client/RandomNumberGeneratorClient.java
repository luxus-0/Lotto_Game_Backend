package pl.lotto.infrastructure.numbergenerator.client;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.lotto.domain.numbersgenerator.RandomNumbersGenerable;
import pl.lotto.domain.numbersgenerator.WinningNumbersConfigurationProperties;
import pl.lotto.domain.numbersgenerator.dto.RandomNumbersDto;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
public class RandomNumberGeneratorClient implements RandomNumbersGenerable {
    private final RestTemplate restTemplate;
    private final WinningNumbersConfigurationProperties properties;

    @Override
    public String generateUniqueTicketId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public RandomNumbersDto generateSixRandomNumbers() {

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        final String url = UriComponentsBuilder.fromHttpUrl(properties.url())
                .queryParam("num", properties.count())
                .queryParam("min", properties.lowerBand())
                .queryParam("max", properties.upperBand())
                .queryParam("format", properties.format())
                .queryParam("col", properties.column())
                .queryParam("base", properties.base())
                .toUriString();

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );
        String body = response.getBody();

        return RandomNumbersDto.builder()
                .randomNumbers(generateRandomNumbers(body))
                .build();
    }


    private Set<Integer> generateRandomNumbers(String body) {
        return Arrays.stream(Objects.requireNonNull(body)
                        .split("\\s+"))
                .map(String::trim)
                .map(Integer::valueOf)
                .collect(Collectors.toSet());
    }
}
