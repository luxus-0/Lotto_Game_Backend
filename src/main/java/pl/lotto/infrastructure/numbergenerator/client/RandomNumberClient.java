package pl.lotto.infrastructure.numbergenerator.client;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.lotto.domain.numbersgenerator.RandomNumbersGenerable;
import pl.lotto.domain.numbersgenerator.WinningNumbersFacadeConfigurationProperties;
import pl.lotto.domain.numbersgenerator.dto.RandomNumbersDto;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class RandomNumberClient implements RandomNumbersGenerable {
    private final RestTemplate restTemplate;
    private final WinningNumbersFacadeConfigurationProperties properties;

    @Override
    public RandomNumbersDto generateRandomNumbers() {
        final String url = UriComponentsBuilder.fromHttpUrl(properties.url_api())
                .queryParam("num", properties.count())
                .queryParam("min", properties.lowerBand())
                .queryParam("max", properties.upperBand())
                .queryParam("format", properties.format())
                .queryParam("col", properties.numberColumn())
                .queryParam("base", properties.base())
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
