package pl.lotto.infrastructure.numbergenerator.client;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.lotto.domain.numbersgenerator.dto.RandomNumberDto;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NumberGeneratorClient {
    private static final String URL = "https://www.random.org/integers/?num=6&min=1&max=99&col=1&base=10&format=plain&rnd=new";

    public WinningNumbersDto generateSixRandomNumbers() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.GET, entity, String.class);

        String responseBody = response.getBody();
        Objects.requireNonNull(responseBody);
        Set<Integer> randomNumbers = getWinningNumbers(responseBody);

        return WinningNumbersDto.builder()
                .winningNumbers(randomNumbers)
                .build();
    }

    private Set<Integer> getWinningNumbers(String responseBody) {
        return Arrays.stream(responseBody.split("\n"))
                .map(Integer::valueOf)
                .collect(Collectors.toSet());
    }
}
