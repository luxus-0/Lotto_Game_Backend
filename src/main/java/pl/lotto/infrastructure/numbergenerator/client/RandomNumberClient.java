package pl.lotto.infrastructure.numbergenerator.client;

import lombok.AllArgsConstructor;
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

@AllArgsConstructor
public class RandomNumberClient implements RandomNumbersGenerable {
    private final RestTemplate restTemplate;

    @Override
    public RandomNumbersDto generateRandomNumbers(RandomNumberParametersUrl parametersUrl) {
        final String url = UriComponentsBuilder.fromHttpUrl(parametersUrl.url())
                .queryParam("num", parametersUrl.count())
                .queryParam("min", parametersUrl.lowerBand())
                .queryParam("max", parametersUrl.upperBand())
                .queryParam("format", parametersUrl.format())
                .queryParam("col", parametersUrl.numberColumn())
                .queryParam("base", parametersUrl.base())
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
