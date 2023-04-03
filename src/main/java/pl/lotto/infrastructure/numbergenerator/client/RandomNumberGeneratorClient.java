package pl.lotto.infrastructure.numbergenerator.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.lotto.domain.numbersgenerator.RandomNumbersGenerable;
import pl.lotto.domain.numbersgenerator.dto.RandomNumbersDto;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RandomNumberGeneratorClient implements RandomNumbersGenerable {
    private final RestTemplate restTemplate;
    private final String url_api;

    @Override
    public RandomNumbersDto generateRandomNumbers(QueryParametersUrl parameters) {
        final String url = UriComponentsBuilder.fromHttpUrl(url_api)
                .queryParam("num", parameters.count())
                .queryParam("min", parameters.lowerBand())
                .queryParam("max", parameters.upperBand())
                .queryParam("format", parameters.format())
                .queryParam("col", parameters.numberColumn())
                .queryParam("base", parameters.base())
                .toUriString();

        String response = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        String.class

                )
                .getBody();

        Objects.requireNonNull(response);
        Set<Integer> randomNumbers = parseResponse(response);

        return RandomNumbersDto.builder()
                .randomNumbers(randomNumbers)
                .build();
    }

    private Set<Integer> parseResponse(String response){
        return Arrays.stream(response.split("\\s+"))
                .map(String::trim)
                .filter(s -> s.matches("\\d"))
                .map(Integer::valueOf)
                .collect(Collectors.toSet());
    }
}
