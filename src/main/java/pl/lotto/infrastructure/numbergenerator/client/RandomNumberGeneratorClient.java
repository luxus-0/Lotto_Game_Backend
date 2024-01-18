package pl.lotto.infrastructure.numbergenerator.client;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.lotto.domain.randomnumbersgenerator.RandomNumbersGenerator;
import pl.lotto.domain.winningnumbers.WinningNumbersConfigurationProperties;
import pl.lotto.domain.winningnumbers.dto.RandomNumbersResponseDto;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Log4j2
public class RandomNumberGeneratorClient implements RandomNumbersGenerator {
    private final RestTemplate restTemplate;
    private final WinningNumbersConfigurationProperties properties;
    private final RandomNumberGeneratorClientValidator validator;

    @Override
    public String generateTicketUUID() {
        return UUID.randomUUID().toString();
    }

    @Override
    public RandomNumbersResponseDto generateRandomNumbers(int count, int lowerBand, int upperBand) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        validator.validateRandomNumbers(count, lowerBand, upperBand);
        ResponseEntity<String> response = makeGetRequest(count, lowerBand, upperBand, requestEntity);
        Set<Integer> randomNumbers = generateRandomNumbers(response.getBody());
        return RandomNumbersResponseDto.builder()
                .randomNumbers(randomNumbers)
                .build();
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
        try {
            return Arrays.stream(Objects.requireNonNull(body)
                            .split("\\s+"))
                    .map(String::trim)
                    .map(Integer::valueOf)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return Set.of(0);
    }
}
