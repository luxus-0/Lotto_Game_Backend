package pl.lotto.infrastructure.numbergenerator.client;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import pl.lotto.domain.numbersgenerator.RandomNumbersGenerable;
import pl.lotto.domain.numbersgenerator.WinningNumbersConfigurationProperties;
import pl.lotto.domain.numbersgenerator.dto.RandomNumbersResponseDto;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Log4j2
public class RandomNumberGeneratorClient implements RandomNumbersGenerable {
    private final RestTemplate restTemplate;
    private final WinningNumbersConfigurationProperties properties;

    @Override
    public String generateUniqueTicketId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public RandomNumbersResponseDto generateRandomNumbers(int count, int lowerBand, int upperBand) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        try {
            ResponseEntity<String> response = makeGetRequest(count, lowerBand, upperBand, requestEntity);
            return RandomNumbersResponseDto.builder()
                    .randomNumbers(generateRandomNumbers(response.getBody()))
                    .build();
        } catch (ResourceAccessException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<String> makeGetRequest(int count, int lowerBand, int upperBand, HttpEntity<String> requestEntity) {
        final String url = UriComponentsBuilder.fromHttpUrl(properties.url())
                .queryParam("num", count)
                .queryParam("min", lowerBand)
                .queryParam("max", upperBand)
                .queryParam("format", properties.format())
                .queryParam("col", properties.column())
                .queryParam("base", properties.base())
                .toUriString();

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
    }


    private Set<Integer> generateRandomNumbers(String body) {
        return Arrays.stream(Objects.requireNonNull(body)
                        .split("\\s+"))
                .map(String::trim)
                .map(Integer::valueOf)
                .collect(Collectors.toSet());
    }
}
